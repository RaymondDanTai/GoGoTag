package com.ggt.dataprocessing;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

import com.ggt.databases.PostsData;

@MultipartConfig()
public class ImageDataProcessing {
	public String getPostImgURL(String dirPath, String tagName, Part imgData, String postId) {

		/* 取得需要變數 */
		String fileExtension = getFileExtension(imgData);
		TimesDataProcessing timesDataProcessing = new TimesDataProcessing();
		String curTime = timesDataProcessing.getCurTime();
		PostsData postsData = new PostsData();
		String posterId = postsData.getPosterId(postId);

		/* 存檔 */
		String fileName = posterId + curTime;

		try {
			imgData.write(dirPath + File.separator + fileName + fileExtension);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* 取得相對路徑 */
		String postImageURL = File.separator + "SocialMedia" + File.separator + tagName + File.separator + fileName
				+ fileExtension;

		return postImageURL;
	}

	public String getFileExtension(Part imgData) {
		String submittedFileName = imgData.getSubmittedFileName();
		int lastDot = submittedFileName.lastIndexOf(".");
		/* 取得檔案副檔名 */
		String fileExtension = submittedFileName.substring(lastDot);
		return fileExtension;
	}

	public String getUserAvatarURL(String dirPath, Part imgData, String userSession) {

		/* 確認目標資料夾是否存在 */
		File fileSaveDir = new File(dirPath);
		if (!fileSaveDir.exists()) {
			fileSaveDir.mkdirs();
		}

		/* 取得需要變數 */
		String fileExtension = getFileExtension(imgData);
		TimesDataProcessing timesDataProcessing = new TimesDataProcessing();
		String curTime = timesDataProcessing.getCurTime();

		/* 存檔 */
		String fileName = userSession + curTime;
		try {
			imgData.write(dirPath + File.separator + fileName + fileExtension);
		} catch (IOException e) {
			e.printStackTrace();
		}

		/* 取得相對路徑 */
		String userAvatarURL = File.separator + "SocialMedia" + File.separator + "users" + File.separator + userSession
				+ File.separator + fileName + fileExtension;

		return userAvatarURL;
	}
	
	public String getCompressedPostImgURL(String dirPath, String tagName, Part imgData, String postId) {
        /* 取得需要變數 */
        String fileExtension = getFileExtension(imgData);
        TimesDataProcessing timesDataProcessing = new TimesDataProcessing();
        String curTime = timesDataProcessing.getCurTime();
        PostsData postsData = new PostsData();
        String posterId = postsData.getPosterId(postId);

        /* 存檔 */
        String fileName = posterId +  curTime;
        
        try {
            BufferedImage inputImage = ImageIO.read(imgData.getInputStream());

            // 壓縮圖片
            BufferedImage compressedImage = compressImage(inputImage, 0.2f); // 壓縮質量為 0.5

            // 將壓縮後的圖片保存到指定目錄
            File outputFile = new File(dirPath, fileName + fileExtension);
            ImageIO.write(compressedImage, fileExtension.substring(1), outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* 取得相對路徑 */
        String postImageURL = File.separator + "SocialMedia" + File.separator + tagName + File.separator + fileName
                + fileExtension;

        return postImageURL;
    }

    private BufferedImage compressImage(BufferedImage inputImage, float compressionQuality) {
        int width = inputImage.getWidth();
        int height = inputImage.getHeight();

        // 創建壓縮後的圖片
        BufferedImage compressedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 繪製壓縮後的圖片
        Graphics2D g2d = compressedImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();

        // 設置壓縮質量
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam params = writer.getDefaultWriteParam();
        params.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        params.setCompressionQuality(compressionQuality);

        // 創建壓縮後的圖片的字節數組
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            writer.setOutput(ImageIO.createImageOutputStream(outputStream));
            writer.write(null, new IIOImage(compressedImage, null, null), params);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            writer.dispose();
        }

        // 讀取壓縮後的圖片
        try {
            return ImageIO.read(new ByteArrayInputStream(outputStream.toByteArray()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
