package net.azureaaron.dandelion.impl.utils;

public final class ReflectionUtils {

	private ReflectionUtils() {}

	public static <T> T createNewDefaultInstance(Class<T> clazz) {
		try {
			return (T) clazz.getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			throw new RuntimeException(String.format("[Dandelion] Class %s must have a no args constructor!", clazz), e);
		}
	}

	/**
	 * {@return the actual class representing an {@code object}}
	 *
	 * <p>Sometimes, Enum constants may be declared with a constant-specific class body which results
	 * in {@code Object#getClass} returning the wrong class when we want the original Enum type.
	 *
	 * <p>For Enums, this returns the class enclosing them (if applicable); for all other types this returns the regular
	 * result of {@code Object#getClass}.
	 */
	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getActualClass(T object) {
		Class<? extends T> originalClass = (Class<? extends T>) object.getClass();
		Class<? extends T> enclosingClass = (Class<? extends T>) originalClass.getEnclosingClass();

		// If the original class is not an enum then check if the enclosing one is and if so return that
		// which allows us to get the actual type of the enum when the constants are defined with anonymous inner classes
		if (!originalClass.isEnum() && enclosingClass != null && enclosingClass.isEnum()) {
			return enclosingClass;
		} else {
			return originalClass;
		}
	}
}
