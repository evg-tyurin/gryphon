package gryphon.common;

/**
 * @author Evgueni Tiourine
 */

public interface Descriptor
{
    int size();

    DescriptorEntry getEntry(int index) throws Exception;

    void addEntry(DescriptorEntry entry) throws Exception;

    String getValue(String key) throws Exception;

    void putValue(String key, String value) throws Exception;
}