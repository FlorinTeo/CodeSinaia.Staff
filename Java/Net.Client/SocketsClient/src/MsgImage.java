import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class MsgImage implements Serializable {
    private static final long serialVersionUID = 1L;
    private BufferedImage _image;
    
    public MsgImage(BufferedImage image) {
        _image = image;
    }
    
    public BufferedImage getImage() {
        return _image;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(_image.toString());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        System.out.println(in.readUTF());
        _image = null;
    }
}
