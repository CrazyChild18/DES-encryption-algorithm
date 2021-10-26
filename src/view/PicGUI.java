package view;
//package com.对话框.文本对话框;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.codec.binary.Base64;

import Controller.DESAlgorithm;

public class PicGUI extends JFrame implements ActionListener {
    
	// 密钥
//	private String key = "7355608";
	
	private JFileChooser fileDialog; // 文件对话框
    private JMenuBar menubar; // 菜单条
    private JMenu menu; // 菜单
    private JMenuItem itemSaveContent, itemOpen, itemSaveEncrypt; // 菜单项
    private JTextArea text, text_out, fileName, inputKey; // 文本区
    private BufferedReader in; // 缓冲读取流
    private FileReader fileReader; // 文件读取流
    private BufferedWriter out; // 缓冲写入流
    private FileWriter fileWriter; // 文件写入流

    private JLabel firstLabel, secondLabel, nameLabel, keyLabel;
    
    
    public PicGUI() {
        init();
        setLayout(new GridLayout(5, 2));
        setSize(1000, 800);
        setVisible(true);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    void init() {
    	keyLabel = new JLabel("", JLabel.CENTER);
    	keyLabel.setSize(600, 50);
    	keyLabel.setText("Input key");
    	add(keyLabel);
    	
    	inputKey = new JTextArea(10, 10);
    	inputKey.setFont(new Font("楷体_gb2312", Font.PLAIN, 28));
        add(inputKey, BorderLayout.CENTER);
    	
    	nameLabel = new JLabel("", JLabel.CENTER);
    	nameLabel.setSize(600, 50);
    	nameLabel.setText("Picture File Name");
		add(nameLabel);
    	
		
		fileName = new JTextArea(2, 10);
		fileName.setFont(new Font("楷体_gb2312", Font.PLAIN, 28));
        add(new JScrollPane(fileName), BorderLayout.CENTER);
		
    	firstLabel = new JLabel("", JLabel.CENTER);
		firstLabel.setSize(600, 50);
		firstLabel.setText("Origin Data");
		add(firstLabel);
        
        secondLabel = new JLabel("", JLabel.CENTER);
        secondLabel.setSize(600, 50);
        secondLabel.setText("Encrypted Content");
		add(secondLabel);
        
		text = new JTextArea(10, 10);
        text.setFont(new Font("楷体_gb2312", Font.PLAIN, 28));
        add(new JScrollPane(text), BorderLayout.CENTER);
		
		text_out = new JTextArea(10, 10);
		text_out.setFont(new Font("楷体_gb2312", Font.PLAIN, 28));
        add(new JScrollPane(text_out), BorderLayout.CENTER);
        
        menubar = new JMenuBar();
        menu = new JMenu("文件");
        itemSaveContent = new JMenuItem("保存原始文件");// 初始化菜单项
        itemSaveEncrypt = new JMenuItem("保存加密文件");// 初始化菜单项
        itemOpen = new JMenuItem("打开文件");
        
        // 注册监视器
        itemSaveContent.addActionListener(this); 
        itemOpen.addActionListener(this);
        itemSaveEncrypt.addActionListener(this);

        // 菜单中加入菜单项
        menu.add(itemOpen);
        menu.add(itemSaveContent);
        menu.add(itemSaveEncrypt);

        menubar.add(menu); // 菜单条中加入菜单
        setJMenuBar(menubar);
        fileDialog = new JFileChooser();
        
        // 加密按钮
        JButton Encryption = new JButton("Start Encryption");
        Encryption.setBackground(Color.GRAY);
      	// 设置尺寸
      	Dimension preferredSize = new Dimension(140, 40);
      	// 设置按钮大小
      	Encryption.setPreferredSize(preferredSize);
      	// 设置按钮垂直对齐方式
      	Encryption.setVerticalAlignment(SwingConstants.CENTER);
      	add(Encryption);
      	Encryption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = inputKey.getText();
		        String content = text.getText();
		        //初始化
		        DESAlgorithm deswanmao = new DESAlgorithm(key); 
		        // 将输入内容转化为byte数组
		        byte[] dataBytes = content.getBytes(Charset.forName("UTF-8"));		        
		        // 调用DesEncrypt函数进行加密，加密代码为1
		        byte[] result = deswanmao.DesStart(dataBytes, 1);
		        // 将加密后的byte转化为string传输展示
		        String atest = Base64.encodeBase64String(result);
		        text_out.setText(new String(atest));
			}
		});
      	
      	
      	// 解密按钮
      	JButton Decode = new JButton("Start Decode");
      	Decode.setBackground(Color.GRAY);
      	// 设置按钮大小
      	Decode.setPreferredSize(preferredSize);
      	// 设置按钮垂直对齐方式
      	Decode.setVerticalAlignment(SwingConstants.CENTER);
      	add(Decode);
      	Decode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = inputKey.getText();
		        String contentDecode = text_out.getText();
		        DESAlgorithm deswanmao = new DESAlgorithm(key);
		        // 将输入内容转化为byte数组
		        byte[] dataBytes = Base64.decodeBase64(contentDecode);
		        // 调用DesEncrypt函数进行加密，解密代码为0
		        byte[] decode_text_byte = deswanmao.DesStart(dataBytes, 0);
		        String decode_textString;
				try {
					decode_textString = new String(decode_text_byte, "utf-8");
					text.setText(decode_textString);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};
				
			}
		});
    }

    // 监听事件
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == itemSaveContent) {
            int state = fileDialog.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                try {
                    // 保存文件
                    File dir = fileDialog.getCurrentDirectory();
                    String name = fileDialog.getSelectedFile().getName();
                    File file = new File(dir, name);
                    FileOutputStream out = new FileOutputStream(file);
                    String codeString = text.getText();
                    byte[] buff = Base64.decodeBase64(codeString);
                    out.write(buff);
                    out.close();
                    text_out.setText(codeString);
                } catch (IOException exp) {
                }
            }
        } else if (e.getSource() == itemOpen) {
            int state = fileDialog.showOpenDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                text.setText(null);
                try {
                    // 打开文件
                    File dir = fileDialog.getCurrentDirectory();
                    String name = fileDialog.getSelectedFile().getName();
                    fileName.setText(name);
                    File file = new File(dir, name);
                    FileInputStream in = new FileInputStream(file);
                    byte[] buff = new byte[in.available()];
                    in.read(buff);
                    in.close();
                    String codeString = Base64.encodeBase64String(buff);
                    text.setText(codeString);
                } catch (IOException exp) {
                }
            }
        }else if (e.getSource() == itemSaveEncrypt) {
        	int state = fileDialog.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                try {
                    // 保存加密文件
                    File dir = fileDialog.getCurrentDirectory();
                    String name = fileDialog.getSelectedFile().getName();
                    System.out.print(name);
                    File file = new File(dir, name);
                    fileWriter = new FileWriter(file);
                    out = new BufferedWriter(fileWriter);
                    out.write(text_out.getText());
                    out.close();
                    fileWriter.close();
                } catch (IOException exp) {
                }
            }
        }
    }
}