package library;

public class ImageSource {
	public static final String[] IMAGES = new String[] {
		"https://lh6.googleusercontent.com/-jZgveEqb6pg/T3R4kXScycI/AAAAAAAAAE0/xQ7CvpfXDzc/s1024/sample_image_01.jpg",
		"https://lh4.googleusercontent.com/-K2FMuOozxU0/T3R4lRAiBTI/AAAAAAAAAE8/a3Eh9JvnnzI/s1024/sample_image_02.jpg",
		"https://lh5.googleusercontent.com/-SCS5C646rxM/T3R4l7QB6xI/AAAAAAAAAFE/xLcuVv3CUyA/s1024/sample_image_03.jpg",
		"https://lh6.googleusercontent.com/-f0NJR6-_Thg/T3R4mNex2wI/AAAAAAAAAFI/45oug4VE8MI/s1024/sample_image_04.jpg"
};

private ImageSource() {
}

public static class Extra {
	public static final String IMAGES = "com.nostra13.example.universalimageloader.IMAGES";
	public static final String IMAGE_POSITION = "com.nostra13.example.universalimageloader.IMAGE_POSITION";
}
}