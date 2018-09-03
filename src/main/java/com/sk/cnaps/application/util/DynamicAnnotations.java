package com.sk.cnaps.application.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DynamicAnnotations {
	private static final Constructor<?> AnnotationInvocationHandler_constructor;
	private static final Constructor<?> AnnotationData_constructor;
	private static final Method Class_annotationData;
	private static final Field Class_classRedefinedCount;
	private static final Field AnnotationData_annotations;
	private static final Field AnnotationData_declaredAnotations;
	private static final Method Atomic_casAnnotationData;
	private static final Class<?> Atomic_class;
	
	private static final Field Field_Field_DeclaredAnnotations;

	static {
		// static initialization of necessary reflection Objects
		try {
			Class<?> AnnotationInvocationHandler_class = Class
					.forName("sun.reflect.annotation.AnnotationInvocationHandler");
			AnnotationInvocationHandler_constructor = AnnotationInvocationHandler_class
					.getDeclaredConstructor(new Class[] { Class.class, Map.class });
			AnnotationInvocationHandler_constructor.setAccessible(true);

			Atomic_class = Class.forName("java.lang.Class$Atomic");
			Class<?> AnnotationData_class = Class.forName("java.lang.Class$AnnotationData");

			AnnotationData_constructor = AnnotationData_class
					.getDeclaredConstructor(new Class[] { Map.class, Map.class, int.class });
			AnnotationData_constructor.setAccessible(true);
			Class_annotationData = Class.class.getDeclaredMethod("annotationData");
			Class_annotationData.setAccessible(true);

			Class_classRedefinedCount = Class.class.getDeclaredField("classRedefinedCount");
			Class_classRedefinedCount.setAccessible(true);

			AnnotationData_annotations = AnnotationData_class.getDeclaredField("annotations");
			AnnotationData_annotations.setAccessible(true);
			AnnotationData_declaredAnotations = AnnotationData_class.getDeclaredField("declaredAnnotations");
			AnnotationData_declaredAnotations.setAccessible(true);

			Atomic_casAnnotationData = Atomic_class.getDeclaredMethod("casAnnotationData", Class.class,
					AnnotationData_class, AnnotationData_class);
			Atomic_casAnnotationData.setAccessible(true);
			
		    Field_Field_DeclaredAnnotations = Field.class.getDeclaredField("declaredAnnotations");
		    Field_Field_DeclaredAnnotations.setAccessible(true);

		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | NoSuchFieldException e) {
			throw new IllegalStateException(e);
		}
	}

	public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass,
			Map<String, Object> valuesMap) {
		putAnnotation(c, annotationClass, annotationForMap(annotationClass, valuesMap));
	}

	public static <T extends Annotation> void putAnnotation(Class<?> c, Class<T> annotationClass, T annotation) {
		try {
			while (true) { // retry loop
				int classRedefinedCount = Class_classRedefinedCount.getInt(c);
				Object /* AnnotationData */ annotationData = Class_annotationData.invoke(c);
				// null or stale annotationData -> optimistically create new instance
				Object newAnnotationData = createAnnotationData(c, annotationData, annotationClass, annotation,
						classRedefinedCount);
				// try to install it
				if ((boolean) Atomic_casAnnotationData.invoke(Atomic_class, c, annotationData, newAnnotationData)) {
					// successfully installed new AnnotationData
					break;
				}
			}
		} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
				| InstantiationException e) {
			throw new IllegalStateException(e);
		}

	}

	@SuppressWarnings("unchecked")
	private static <T extends Annotation> Object /* AnnotationData */ createAnnotationData(Class<?> c,
			Object /* AnnotationData */ annotationData, Class<T> annotationClass, T annotation, int classRedefinedCount)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<Class<? extends Annotation>, Annotation> annotations = (Map<Class<? extends Annotation>, Annotation>) AnnotationData_annotations
				.get(annotationData);
		Map<Class<? extends Annotation>, Annotation> declaredAnnotations = (Map<Class<? extends Annotation>, Annotation>) AnnotationData_declaredAnotations
				.get(annotationData);

		Map<Class<? extends Annotation>, Annotation> newDeclaredAnnotations = new LinkedHashMap<>(annotations);
		newDeclaredAnnotations.put(annotationClass, annotation);
		Map<Class<? extends Annotation>, Annotation> newAnnotations;
		if (declaredAnnotations == annotations) {
			newAnnotations = newDeclaredAnnotations;
		} else {
			newAnnotations = new LinkedHashMap<>(annotations);
			newAnnotations.put(annotationClass, annotation);
		}
		return AnnotationData_constructor.newInstance(newAnnotations, newDeclaredAnnotations, classRedefinedCount);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Annotation> T annotationForMap(final Class<T> annotationClass,
			final Map<String, Object> valuesMap) {
		return (T) AccessController.doPrivileged(new PrivilegedAction<Annotation>() {
			public Annotation run() {
				InvocationHandler handler;
				try {
					handler = (InvocationHandler) AnnotationInvocationHandler_constructor.newInstance(annotationClass,
							new HashMap<>(valuesMap));
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException e) {
					throw new IllegalStateException(e);
				}
				return (Annotation) Proxy.newProxyInstance(annotationClass.getClassLoader(),
						new Class[] { annotationClass }, handler);
			}
		});
	}

	/**
	 * Add annotation to Field<br>
	 * Note that you may need to give the root field.
	 *
	 * @param field
	 * @param annotation
	 * @author XDean
	 * @see java.lang.reflect.Field
	 * @see #createAnnotationFromMap(Class, Map)
	 * @see ReflectUtil#getRootFields(Class)
	 */
	@SuppressWarnings("unchecked")
	public static void addAnnotation(Field field, Annotation annotation) {
		field.getAnnotation(Annotation.class);// prevent declaredAnnotations haven't initialized
		Map<Class<? extends Annotation>, Annotation> annos;
		try {
			annos = (Map<Class<? extends Annotation>, Annotation>) Field_Field_DeclaredAnnotations.get(field);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(e);
		}
		if (annos.getClass() == Collections.EMPTY_MAP.getClass()) {
			annos = new HashMap<>();
			try {
				Field_Field_DeclaredAnnotations.set(field, annos);
			} catch (IllegalAccessException e) {
				throw new IllegalStateException(e);
			}
		}
		annos.put(annotation.annotationType(), annotation);
	}
	
	/**
	 * Changes the annotation value for the given key of the given annotation to newValue and returns
	 * the previous value.
	 */
	@SuppressWarnings("unchecked")
	public static Object changeAnnotationValue(Annotation annotation, String key, Object newValue){
	    Object handler = Proxy.getInvocationHandler(annotation);
	    Field f;
	    try {
	        f = handler.getClass().getDeclaredField("memberValues");
	    } catch (NoSuchFieldException | SecurityException e) {
	        throw new IllegalStateException(e);
	    }
	    f.setAccessible(true);
	    Map<String, Object> memberValues;
	    try {
	        memberValues = (Map<String, Object>) f.get(handler);
	    } catch (IllegalArgumentException | IllegalAccessException e) {
	        throw new IllegalStateException(e);
	    }
	    Object oldValue = memberValues.get(key);
	    if (oldValue == null || oldValue.getClass() != newValue.getClass()) {
	        throw new IllegalArgumentException();
	    }
	    memberValues.put(key,newValue);
	    return oldValue;
	}
}
