package net.azureaaron.dandelion.impl.utils;

public class ReflectionUtils {

	public static <T> T createNewDefaultInstance(Class<T> clazz) {
		try {
			return (T) clazz.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(String.format("[Dandelion] Class {} must have a no args constructor!", clazz), e);
		}
	}

	/**
	 * Retrieves the actual class representing an {@code object}. Sometimes, enums constants may be declared with a constant-specific
	 * class body which results in {@code Object#getClass} returning the wrong class since when want the original enum type.
	 * 
	 * For enums, this returns the class enclosing them; for all other types this returns the regular result of {@code Object#getClass}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getActualClass(T object) {
		Class<? extends T> enclosingClass = (Class<? extends T>) object.getClass().getEnclosingClass();

		if (enclosingClass != null && enclosingClass.isEnum()) {
			return enclosingClass;
		} else {
			return (Class<? extends T>) object.getClass();
		}
	}
}
