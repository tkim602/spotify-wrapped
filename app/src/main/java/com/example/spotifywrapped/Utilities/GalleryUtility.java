package com.example.spotifywrapped.Utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GalleryUtility {
    public static ArrayList<String> extractBase64Strings(String text) {
        if (text.isEmpty()) {
            return new ArrayList<String>();
        }
        String[] result = text.split("\\s+");
        return new ArrayList<>(Arrays.asList(result));
    }

    public static String encodeBase64ImagesToString(ArrayList<String> images) {
        return String.join(" ", images);
    }
}