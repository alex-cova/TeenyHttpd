package net.jonathangiles.tools.teenyhttpd.implementation;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;

/** A helper class to hold the type arguments of a parameterized type and provide some utility methods. */
public final class ParameterizedTypeHelper implements Type {
    private final Class<?> firstType;
    private final Class<?> parentType;//nullable
    private final Type[] typeArguments;

    ParameterizedTypeHelper(Class<?> type) {
        this.firstType = type;
        this.parentType = null;
        this.typeArguments = null;
    }

    public ParameterizedTypeHelper(Class<?> firstType, Class<?> parentType) {
        this.firstType = firstType;
        this.parentType = parentType;
        this.typeArguments = null;
    }

    ParameterizedTypeHelper(Class<?> parentType, Type[] arguments) {
        this.firstType = getRealClass(arguments[0]);
        this.parentType = parentType;
        this.typeArguments = arguments;
    }

    private Class<?> getRealClass(Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            return (Class<?>) wildcardType.getUpperBounds()[0];
        }

        return (Class<?>) type;
    }

    public ParameterizedTypeHelper withFirstType(Class<?> firstType) {
        if (typeArguments == null) {
            return new ParameterizedTypeHelper(firstType, parentType);
        }

        Type[] args = new Type[typeArguments.length];

        args[0] = firstType;
        System.arraycopy(typeArguments, 1, args, 1, typeArguments.length - 1);

        return new ParameterizedTypeHelper(parentType, args);
    }

    @Override
    public String getTypeName() {
        return getClass().getSimpleName();
    }

    public boolean isParentTypeOf(Class<?> type) {
        return parentType != null && parentType.isAssignableFrom(type);
    }

    public Type[] getTypeArguments() {
        return typeArguments;
    }

    public Class<?> getFirstType() {
        return firstType;
    }

    public Class<?> getSecondType() {
        Objects.requireNonNull(typeArguments, "Type is not a ParameterizedType");
        return getRealClass(typeArguments[1]);
    }

    public Class<?> getParentType() {
        return parentType;
    }

    @Override
    public String toString() {
        return "ParameterizedTypeHelper{" +
                "firstType=" + firstType +
                ", parentType=" + parentType +
                ", typeArguments=" + Arrays.toString(typeArguments) +
                '}';
    }
}