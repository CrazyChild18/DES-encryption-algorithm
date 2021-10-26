package view;
//package com.�Ի���.�ı��Ի���;
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
    
	// ��Կ
//	private String key = "7355608";
	
	private JFileChooser fileDialog; // �ļ��Ի���
    private JMenuBar menubar; // �˵���
    private JMenu menu; // �˵�
    private JMenuItem itemSaveContent, itemOpen, itemSaveEncrypt; // �˵���
    private JTextArea text, text_out, fileName, inputKey; // �ı���
    private BufferedReader in; // �����ȡ��
    private FileReader fileReader; // �ļ���ȡ��
    private BufferedWriter out; // ����д����
    private FileWriter fileWriter; // �ļ�д����

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
    	inputKey.setFont(new Font("����_gb2312", Font.PLAIN, 28));
        add(inputKey, BorderLayout.CENTER);
    	
    	nameLabel = new JLabel("", JLabel.CENTER);
    	nameLabel.setSize(600, 50);
    	nameLabel.setText("Picture File Name");
		add(nameLabel);
    	
		
		fileName = new JTextArea(2, 10);
		fileName.setFont(new Font("����_gb2312", Font.PLAIN, 28));
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
        text.setFont(new Font("����_gb2312", Font.PLAIN, 28));
        add(new JScrollPane(text), BorderLayout.CENTER);
		
		text_out = new JTextArea(10, 10);
		text_out.setFont(new Font("����_gb2312", Font.PLAIN, 28));
        add(new JScrollPane(text_out), BorderLayout.CENTER);
        
        menubar = new JMenuBar();
        menu = new JMenu("�ļ�");
        itemSaveContent = new JMenuItem("����ԭʼ�ļ�");// ��ʼ���˵���
        itemSaveEncrypt = new JMenuItem("��������ļ�");// ��ʼ���˵���
        itemOpen = new JMenuItem("���ļ�");
        
        // ע�������
        itemSaveContent.addActionListener(this); 
        itemOpen.addActionListener(this);
        itemSaveEncrypt.addActionListener(this);

        // �˵��м���˵���
        menu.add(itemOpen);
        menu.add(itemSaveContent);
        menu.add(itemSaveEncrypt);

        menubar.add(menu); // �˵����м���˵�
        setJMenuBar(menubar);
        fileDialog = new JFileChooser();
        
        // ���ܰ�ť
        JButton Encryption = new JButton("Start Encryption");
        Encryption.setBackground(Color.GRAY);
      	// ���óߴ�
      	Dimension preferredSize = new Dimension(140, 40);
      	// ���ð�ť��С
      	Encryption.setPreferredSize(preferredSize);
      	// ���ð�ť��ֱ���뷽ʽ
      	Encryption.setVerticalAlignment(SwingConstants.CENTER);
      	add(Encryption);
      	Encryption.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = inputKey.getText();
		        String content = text.getText();
		        //��ʼ��
		        DESAlgorithm deswanmao = new DESAlgorithm(key); 
		        // ����������ת��Ϊbyte����
		        byte[] dataBytes = content.getBytes(Charset.forName("UTF-8"));		        
		        // ����DesEncrypt�������м��ܣ����ܴ���Ϊ1
		        byte[] result = deswanmao.DesStart(dataBytes, 1);
		        // �����ܺ��byteת��Ϊstring����չʾ
		        String atest = Base64.encodeBase64String(result);
		        text_out.setText(new String(atest));
			}
		});
      	
      	
      	// ���ܰ�ť
      	JButton Decode = new JButton("Start Decode");
      	Decode.setBackground(Color.GRAY);
      	// ���ð�ť��С
      	Decode.setPreferredSize(preferredSize);
      	// ���ð�ť��ֱ���뷽ʽ
      	Decode.setVerticalAlignment(SwingConstants.CENTER);
      	add(Decode);
      	Decode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = inputKey.getText();
		        String contentDecode = text_out.getText();
		        DESAlgorithm deswanmao = new DESAlgorithm(key);
		        // ����������ת��Ϊbyte����
		        byte[] dataBytes = Base64.decodeBase64(contentDecode);
		        // ����DesEncrypt�������м��ܣ����ܴ���Ϊ0
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

    // �����¼�
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == itemSaveContent) {
            int state = fileDialog.showSaveDialog(this);
            if (state == JFileChooser.APPROVE_OPTION) {
                try {
                    // �����ļ�
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
                    // ���ļ�
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
                    // ��������ļ�
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