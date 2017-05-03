import com.docgl.Cryptor;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by Client on 3.5.2017.
 */
public class CryptorTest {

    @Test
    public void encryptTest() {
        String encripted = Cryptor.encrypt("Ras#2d");
        assertEquals("f02700614fc776b6bac00b8853264abf", encripted);
    }

    @Test
    public void decryptTest() {
        String decrypted = Cryptor.decrypt("f02700614fc776b6bac00b8853264abf");
        assertEquals("Ras#2d", decrypted);
    }

    @Test
    public void cryptorTest() {
        String encripted = Cryptor.encrypt("Ras#2d");
        String decrypted =  Cryptor.decrypt("f02700614fc776b6bac00b8853264abf");
        assertEquals(Cryptor.encrypt(decrypted), encripted);
        assertEquals(Cryptor.decrypt(encripted), decrypted);
    }
}
