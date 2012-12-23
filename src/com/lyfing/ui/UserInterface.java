package com.lyfing.ui;
 
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
 
public class UserInterface extends JPanel {
	protected JTextField classNum ;
	protected JTextField stuid ;
	protected JPasswordField pwd ;
	protected JTextField userInfo ;
    protected JTextArea taInfo ; // 显示用户信息和相关课程信息；
    protected JTextArea taIsLegal ; // 登陆框上显示用户名、密码错误；
    protected String newline = "\r\n" ;
    protected JCheckBoxMenuItem[] cbmi ;
    protected static JFrame frame ;
    protected JButton butLogin , butReset , butChengJi , butCurrentGPA ;
    protected JCheckBox jcb ;
    protected JScrollPane scrollPane ;
    protected JMenuItem menuItemLogout ;
    private String TeamName = "TheNUCerOSP" ;
    private String Title = "课程&成绩信息平台" ;
    private String FinalName = TeamName + "_" + Title ;
    int screenWidth =(int)Toolkit.getDefaultToolkit().getScreenSize().getWidth() ; 
    int screenHeight =(int)Toolkit.getDefaultToolkit().getScreenSize().getHeight() ; 
// 在此设立两个构造方法，首先是登陆界面，如果成绩验证成功，则再调用主界面的构造方法；   

// 【登陆界面】的构造方法
    public UserInterface(String title) {
    	super(new BorderLayout());
        frame = new JFrame( FinalName );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
/* this关键字的理解有些模糊，所以百度了下面的东西
 * 调用对象的成员方法的时候，我们要使用 对象实例名.成员名的格式.
 * 很显然，在调用对象的成员方法时，一定会伴随有某个对象实例，
 * this关键字所代表的就是在调用对象的成员方法时，
 * 引用该方法的当前对象实例（也就是说this代表的是一个对象实例，
 * 那这个this代表的是哪个对象实例呢，
 * 其实就是正在调用成员方法的这个对象实例   
 * 
 */     int width = 500 , height = 300  ;
 		AListner listner = new AListner() ;
 		
    	this.setLayout(null);
    	//Lable1
    	JLabel label1=new JLabel("用户登录");
    	label1.setBounds(180,10,100,60);
    	label1.setFont(new Font(null,Font.BOLD|Font.ITALIC,24));
    	this.add(label1);
    	//label
    	JLabel label = new JLabel("班级号");
    	label.setBounds(130,80,60,25);
    	this.add(label);
    	//TextField
    	classNum = new JTextField();
    	classNum.setBounds(235,80,100,25);
    	classNum.setText("09060242");
    	classNum.addFocusListener( listner ) ;
    	this.add(classNum);
    	//label2
    	JLabel label2=new JLabel("学号(2位数)");
    	label2.setBounds(130, 110, 100, 25);
    	this.add(label2);
    	//TextField
    	stuid=new JTextField(2);
    	stuid.setBounds(235, 110, 100, 25);
    	this.add(stuid);
    	//label3
    	JLabel label3=new JLabel("密码");
    	label3.setBounds(130, 140, 100, 25);
    	this.add(label3);
    	//TextField
    	pwd=new JPasswordField(10);
    	pwd.setBounds(235, 140, 100, 25);
    	pwd.addFocusListener( listner ) ;
    	this.add(pwd);
    	// textArea 显示用户名或密码错误;
    	taIsLegal = new JTextArea();
    	taIsLegal.setBounds(130,170,120,20);
    	taIsLegal.setVisible(false);
    	this.add(taIsLegal);
    	//用来勾选是否记住密码
    	jcb = new JCheckBox("记住我") ;
    	jcb.setBounds( 275 , 170 , 80, 20) ;
    	this.add( jcb ) ;
    	//Button
    	butLogin=new JButton(" 登录 ");
    	butLogin.setBounds(140 ,200 , 80, 35);
    	this.add(butLogin);
    	butReset=new JButton(" 重置 ");
    	butReset.setBounds(250, 200, 80, 35);
    	this.add(butReset);
        frame.setContentPane(this);
        frame.setBounds((screenWidth-width)/2, (screenHeight-height)/2-100 , width, height);
        frame.setBackground( new Color(205,201,14)) ;
    	frame.setVisible(true);
    } //登陆界面的构造方法；
    
// 【功能主界面】的构造方法
    public UserInterface() {
        super(new BorderLayout());
        
        //Create a scrolled text area 
        taInfo = new JTextArea(5,30) ;
        taInfo.setEditable( false );
        taInfo.setLineWrap(true); //设置是否自动换行
        taInfo.setFont( new Font( null, Font.PLAIN , 15) ) ;
        scrollPane = new JScrollPane( taInfo ) ;
       
        //Lay out the content pane.
        setPreferredSize(new Dimension(600, 500));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    public JMenuBar createMenuBar() {
    	JMenuBar menuBar = new JMenuBar() ;
    	JMenu mainMenu = new JMenu("Main") ;
    	JMenu othersMenu = new JMenu( "Others" ) ;
        JMenuItem menuItem = null;
        
        menuItemLogout = new JMenuItem("切换用户") ;
        menuItemLogout.setAccelerator(KeyStroke.getKeyStroke( KeyEvent.VK_Q, ActionEvent.ALT_MASK));
        mainMenu.add( menuItemLogout ) ;
        
        menuItem = new JMenuItem( "退出" ) ;
        menuItem.setActionCommand("exit") ;
        menuItem.addActionListener( new AListner() ) ;
        mainMenu.add( menuItem ) ;
        
        menuItem = new JMenuItem( "关于" ) ;
        menuItem.setActionCommand( "about" ) ;
        menuItem.addActionListener( new AListner() ) ;
        othersMenu.add( menuItem ) ;
        
        menuBar.add( mainMenu ) ;
        menuBar.add( othersMenu ) ;
        return menuBar;
    }

    public void createToolBar() { 
        //Create the toolbar.
        JToolBar toolBar = new JToolBar();
        add(toolBar, BorderLayout.PAGE_START);
        //second button
        butChengJi = new JButton ( "=本科生成绩=" ) ;
        toolBar.add(butChengJi);
        toolBar.addSeparator() ;
        //third button
        butCurrentGPA = new JButton ("=已修课程GPA=") ;
        toolBar.add(butCurrentGPA);   
        toolBar.setFloatable( false ) ;
    }

    public UserInterface createGUI() {
        //Create and set up the window.
        //Create/set menu bar and content pane.
    	int width = 500 ; int height = 500 ;
    	UserInterface ui = new UserInterface();

        frame = new JFrame(FinalName);
        frame.setBounds((screenWidth-width)/2, (screenHeight-height)/2-30 , width , height);
        frame.setJMenuBar(ui.createMenuBar());
        ui.createToolBar();
        ui.setOpaque(true); //content panes must be opaque

        frame.setContentPane(ui);
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        return ui ;
    }
     
    public class AListner implements ActionListener , FocusListener {
		public void focusGained(FocusEvent e) {
			if ( e.getComponent().equals( pwd ) ) {
				pwd.selectAll() ;
			} else if ( e.getComponent().equals( classNum )) {
				classNum.selectAll() ;
			}
		}
		public void focusLost(FocusEvent e) {
			
		}
		public void actionPerformed(ActionEvent e) {
			if ( e.getActionCommand().equals("about") ) {
				taInfo.setText( aboutClassesScoreProject ) ;
				taInfo.append( aboutMoreDetails ) ;
			} else if ( e.getActionCommand().equals( "exit") ) {
				System.exit( 0 ) ;
			}
		}
    	
    }
    
    
    final private String aboutClassesScoreProject ="关于【" + FinalName + "】" + newline +
    		"【" + FinalName + "】致力于为广大中北学子构建一个一站式的" + newline +
    		"个人课程+成绩的信息应用平台。" + newline + newline +
    		"【我们的愿景是】：" + newline +
    		"借此应用，您可以在每年的选课季直接选择自己喜欢的任选课、体育课等相关课程；" + newline +
    		"您还可以在此查看、管理自己所有的课程信息(比如查看每科成绩和已修课程平均学分绩点，查分前还可以让软件自动完成本学期的教学评估等)；" + newline +
    		"您还可以在挂科补考等相关\"挂科\"的信息出现在校网站后，及时接收到是否补报课程及报名考试等相关信息，并可直接在此补报课程..." + newline + newline +
    		"至于该功能的Android应用，我们更期待着各位牛人的加入(本程序完全用java开发，因此移植到Android平台会很方便)！" + newline + newline +
    		"尤其需要注意的是，所有这些功能应用，全部都是开源的！" + newline + 
    		"因此您完全可以根据自己的喜好，增加、改动、移除某些模块应用！" + newline ;
    final private String aboutMoreDetails = "==========\r\n欢迎访问" + newline + 
    										"\"TheNUCerOSP\"的微博获得更多信息:" + newline +
    										"weibo.com/TheNUCerOSP";
    final private String aboutOSPProject ="";
    

} //class UserInterface



/** 
 * 下面的方法用来给jtoolbar设置图标，但太麻烦，暂时搁置备用；
、
protected static ImageIcon createNavigationIcon(String imageName) {
    String imgLocation = imageName + ".gif";
    java.net.URL imageURL = UserInterface.class.getResource("/" + imgLocation);

    if (imageURL == null) {
        System.err.println("Resource not found:"
                           + imgLocation);
        return null;
    } else {
        return new ImageIcon(imageURL);
    }
}*/

/*    public UserInterface showGUI() {
//Schedule a job for the event-dispatching thread:
//creating and showing this application's GUI.
class start implements Runnable {
	public void run() {
		ui = createGUI() ;
	}
	
}
UserInterface ui = null ;
javax.swing.SwingUtilities.invokeLater ( new start()) ;
	
return ui ;
} //showGUI();
*/  