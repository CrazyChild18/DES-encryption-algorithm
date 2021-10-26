package view;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import org.apache.commons.codec.binary.Base64;

import Controller.DESAlgorithm;


public class TextGUI extends JFrame { 
   
	// ��Կ
//	private String key = "7355608";
	
	private JTextArea inputText, byteText, showText, inputKey;
	private JLabel firstLabel, secondLabel, thirdLabel, keyLabel;
   
	public TextGUI() { 
		init();
		setLayout(new GridLayout(8, 1));
		setVisible(true);
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);   
	}
   
	private void init() {
		keyLabel = new JLabel("", JLabel.CENTER);
		keyLabel.setSize(600, 50);
		keyLabel.setText("Input key");
		
		
		firstLabel = new JLabel("", JLabel.CENTER);
		firstLabel.setSize(600, 50);
		firstLabel.setText("Original Content Input");
		
		thirdLabel = new JLabel("",JLabel.CENTER);
		thirdLabel.setSize(600, 50);
		thirdLabel.setText("Encrypted Ciphertext");
		
		inputKey = new JTextArea(7, 50);
		
		inputText = new JTextArea(7, 50);
		showText = new JTextArea(7, 50);
		// �����Զ����й��� 
		inputText.setLineWrap(true);
		showText.setLineWrap(true);
		// ������в����ֹ���
		inputText.setWrapStyleWord(true);
		showText.setWrapStyleWord(true);
		add(keyLabel);
		add(inputKey);
		add(firstLabel);
      	add(new JScrollPane(inputText));
      	add(thirdLabel);
      	add(new JScrollPane(showText));
      	
      	
      	// ��Ӽ��ܰ�ť
      	JButton start = new JButton("Start Encryption");
      	start.setBackground(Color.GRAY);
      	// ���óߴ�
      	Dimension preferredSize = new Dimension(140, 40);
      	// ���ð�ť��С
      	start.setPreferredSize(preferredSize);
      	// ���ð�ť��ֱ���뷽ʽ
      	start.setVerticalAlignment(SwingConstants.CENTER);
      	add(start);
      	start.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = inputKey.getText();
				String content = inputText.getText();
				//��ʼ��
		        DESAlgorithm des = new DESAlgorithm(key);
		        // ����������ת��Ϊbyte����
		        byte[] dataBytes = content.getBytes(Charset.forName("UTF-8"));		        
		        // ����DesStrat�������м��ܣ����ܴ���Ϊ1
		        byte[] result = des.DesStart(dataBytes, 1);
		        // �����ܺ��byteת��Ϊstring����չʾ
		        String atest = Base64.encodeBase64String(result);
		        showText.setText(new String(atest));
			}
		});
      	
      	
      	// ��ӽ��ܰ�ť
      	JButton decode = new JButton("Start Decode");
      	decode.setBackground(Color.GRAY);
      	// ���óߴ�
      	Dimension preferredSize2 = new Dimension(140, 40);
      	// ���ð�ť��С
      	decode.setPreferredSize(preferredSize2);
      	// ���ð�ť��ֱ���뷽ʽ
      	decode.setVerticalAlignment(SwingConstants.CENTER);
      	add(decode);
      	decode.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String key = inputKey.getText();
				String contentDecode = showText.getText();
		        DESAlgorithm des = new DESAlgorithm(key);
		        // ����������ת��Ϊbyte����
		        byte[] dataBytes = Base64.decodeBase64(contentDecode);
//		        for(byte ea3 : dataBytes) {
//					System.out.print(ea3 + " ");
//		        }

		        // ����DesEncrypt�������м��ܣ����ܴ���Ϊ0
		        byte[] decode_text_byte = des.DesStart(dataBytes, 0);
//		        System.out.println(dataBytes);
//		        for(byte ea3 : dataBytes) {
//					System.out.print(ea3 + " ");
//		        }
		        
		        String decode_textString;
				try {
					decode_textString = new String(decode_text_byte, "utf-8");
					inputText.setText(decode_textString);
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				};
		        
		        
			}
		});
      	
   }
   
   

   
}