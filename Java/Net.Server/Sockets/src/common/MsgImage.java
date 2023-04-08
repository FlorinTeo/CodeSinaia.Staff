package common;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MsgImage implements Serializable {
    private static final long serialVersionUID = 1L;
    private BufferedImage _image;
    
    public MsgImage(BufferedImage image) throws IOException {
        _image = image;
    }
    
    public BufferedImage getImage() {
        return _image;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        byte[] imgBytes = Helpers.imageToBytes(_image);
        out.writeObject(imgBytes);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        byte[] imgBytes = (byte[])in.readObject();
        _image = Helpers.bytesToImage(imgBytes);
    }
}
