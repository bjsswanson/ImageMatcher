package com.swansonb.imagematching;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ImagePanel extends JPanel {
	private BufferedImage image;

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public void setImage(Mat matrix) {
		this.image = matToBufferedImage(matrix);
	}

	public void paintComponent(Graphics g){
		BufferedImage temp = getImage();
		if(temp != null){
			g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);
		}
	}

	public static BufferedImage matToBufferedImage(Mat matrix) {
		MatOfByte mb=new MatOfByte();
		Highgui.imencode(".jpg", matrix, mb);
		try {
			return ImageIO.read(new ByteArrayInputStream(mb.toArray()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null; // Successful
	}

}
