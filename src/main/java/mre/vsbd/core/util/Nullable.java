package mre.vsbd.core.util;

public interface Nullable<T>
{
    T get() throws Exception;

    static <T> T value(final Nullable<T> value)
    {
        try
        {
            return value.get();
        }
        catch (final Exception _)
        {
            return null;
        }
    }
}
