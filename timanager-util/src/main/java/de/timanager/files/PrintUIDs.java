package de.timanager.files;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class PrintUIDs extends ObjectInputStream {

    public PrintUIDs(InputStream in) throws IOException {
        super(in);
    }

    @Override
    protected ObjectStreamClass readClassDescriptor() throws IOException,
            ClassNotFoundException {
        ObjectStreamClass descriptor = super.readClassDescriptor();
        System.out.println("name=" + descriptor.getName());
        System.out.println("serialVersionUID=" + descriptor.getSerialVersionUID());
        return descriptor;
    }
}