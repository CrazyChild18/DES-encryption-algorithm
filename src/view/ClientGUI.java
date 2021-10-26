package view;

import java.awt.*;
import java.awt.event.*;
 
import javax.swing.*;
 


public class ClientGUI extends JFrame implements ActionListener, KeyListener {
 
		
	//Accept message box
	private JTextField sendMessage = new JTextField();
	//Display information box
	private JTextArea showMessage = new JTextArea();

	
	public static void main(String[] args) {
		ClientGUI win = new ClientGUI("DES", 20, 30, 400, 500);
	}
	
	/**
	 * Client constructor, drawing interface
	 */
	JMenuBar menubar;
    JMenu menu, subMenu;
    JMenuItem item1, item2, item3, item4, item5;

	
	// 得到显示器高度
	public int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	public int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	// 定义框体宽度
	public int windowsWedth = 700;
	public int windowsHeight = 800;
	
	public ClientGUI(String s, int x, int y, int w, int h) {
		
		init(s);
        setLocation(x, y);
        setSize(w, h);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}
	
	public void init(String s) {
        setTitle(s);
        menubar = new JMenuBar();
        // JMnud的实例就是一个菜单
        menu = new JMenu("Selection of encrypted content");
        // 创建菜单项
        item1 = new JMenuItem("Text Encryption"); 
        item2 = new JMenuItem("Image Encryption");
        item4 = new JMenuItem("TXT File Encryption");
//        item1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.CTRL_MASK));
//        item2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_2, InputEvent.CTRL_MASK));
//        item3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_3, InputEvent.CTRL_MASK));
//        item4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_4, InputEvent.CTRL_MASK));
        menu.add(item1);
        menu.addSeparator();
        menu.add(item2);
        menu.addSeparator();
        menu.add(item4);
        // 文字加密事件
        item1.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent e) {
    			super.mouseClicked(e);
    			TextGUI textGUI = new TextGUI();
    			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    		    int x = (int) ((dimension.getWidth() - 700) / 2);
    		    int y = (int) ((dimension.getHeight() - 800) / 2);
    			textGUI.setBounds(x, y, 700, 800);
    			textGUI.setTitle("Text Encryption");
    		}
    	});
        // 图片加密事件
        item2.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent e) {
    			super.mouseClicked(e);
    			PicGUI picGUI = new PicGUI();
    			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    		    int x = (int) ((dimension.getWidth() - 1000) / 2);
    		    int y = (int) ((dimension.getHeight() - 850) / 2);
    		    picGUI.setTitle("Image Encryption");
    		    picGUI.setBounds(x, y, 1000, 800);
    		}
    	});
        // TXT加密事件
        item4.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent e) {
    			super.mouseClicked(e);
    			TxtGUI txtGUI = new TxtGUI();
    			Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
    		    int x = (int) ((dimension.getWidth() - 1000) / 2);
    		    int y = (int) ((dimension.getHeight() - 850) / 2);
    		    txtGUI.setTitle("TXT File Encryption");
    		    txtGUI.setBounds(x, y, 1000, 800);
    		}
    	});
        menubar.add(menu); // 菜单条中加入菜单
        setJMenuBar(menubar); // 添加一个菜单条

    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
 
}
