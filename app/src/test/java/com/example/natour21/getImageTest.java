package com.example.natour21;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.example.natour21.Utils.ImagePicker;
import org.junit.Test;

public class getImageTest {

    String t1 = "mik";
    String t2 = "mike";
    String t3 = "mike.fonseta171217";
    String t4 = "mike.fonseta17121719235461746231";
    String t5 = "mike.fonseta171217192354617462318712625";

    @Test
    public void test1(){
        assertEquals(ImagePicker.getImage(t1), R.drawable.user1);
    }

    @Test
    public void test2(){
        assertEquals(ImagePicker.getImage(t2), R.drawable.user1);
    }

    @Test
    public void test3(){
        assertEquals(ImagePicker.getImage(t3), R.drawable.user8);
    }

    @Test
    public void test4(){
        assertEquals(ImagePicker.getImage(t4), R.drawable.user12);
    }

    @Test
    public void test5(){
        assertEquals(ImagePicker.getImage(t5), R.drawable.user1);
    }
}



