package com.example.natour21;

import com.example.natour21.Utils.ImagePicker;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ImagePickerTest {

    String nullUsername = null;
    String lessThen4 = "mik";
    String validUsername = "mike.fonseta";
    String gratherThan32 = "mike.fonseta171317007254361739102831273849171217";

    @Test
    public void NullUsername()
    {
        assertEquals(R.drawable.user1, ImagePicker.getImage(nullUsername));
    }

    @Test
    public void UsernameLenghtLessThan4()
    {
        assertEquals(R.drawable.user1, ImagePicker.getImage(lessThen4));
    }

    @Test
    public void ValidUsername()
    {
        assertEquals(R.drawable.user5, ImagePicker.getImage(validUsername));
    }

    @Test
    public void UsernameLenghtGratherThan32()
    {
        assertEquals(R.drawable.user1, ImagePicker.getImage(gratherThan32));
    }

}