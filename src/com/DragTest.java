package com;

/**
 * @author CGB
 * @date 2020/12/16 13:44
 * @Version v1.0
 */

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

public class DragTest extends JFrame {

    JPanel panel;//Ҫ������ק�����

    public DragTest() {
        panel = new JPanel();
        panel.setBackground(Color.WHITE);
        getContentPane().add(panel, BorderLayout.CENTER);
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(600, 500);
        setLocationRelativeTo(null);
        setTitle("cgb2080@163.com");
        drag();//������ק
    }

    public static void main(String[] args) throws Exception {
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");//����Ƥ��
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");//����Ƥ��
//        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel");//����Ƥ��
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());//����Ƥ��
        new DragTest().setVisible(true);
        ;
    }

    public void drag()//�������ק����
    {
        //panel��ʾҪ������ק�Ŀؼ�
        new DropTarget(panel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde)//��д��������drop����
            {
                try {
                    FileSystemView fsv = FileSystemView.getFileSystemView();
                    File com = fsv.getHomeDirectory();
                    System.out.println(com.getPath());
                    String baseDir = com.getPath();
                    File file1 = new File(baseDir + "/" + "start.bat");
                    if (!file1.exists()) {
                        file1.createNewFile();
                    }
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//���������ļ���ʽ��֧��
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//������ק��������
                        List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        String temp = "";
//                        BufferedReader br = new BufferedReader(new FileReader(file1));
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file1),"gb2312"));
                        Set<String> set = new LinkedHashSet<>();

                        String lineTxt = null;
                        while ((lineTxt = br.readLine()) != null) {
                            System.out.println(lineTxt);
                            set.add(lineTxt);
                        }
                        set.remove("@echo off");
                        set.remove("pause");
                        for (File file : list) {
                            if (file.isFile()) {
                                String fileName = file.getName();
                                String path = file.getParent();
//                                temp = path +"+++++++" +fileName;

                                String name = fileName.substring(0,fileName.lastIndexOf("."));
                                String desc = new StringBuffer().append("echo \"�������� ").append(name).append("\"").toString();
                                String title = new StringBuffer().append("::      ").append(name).toString();
                                String start = new StringBuffer().append("start /B  call ").append("\"").append(path).append("\\").append(fileName).append("\"").toString();
                                if (set.contains(desc)){
                                    set.remove(desc);
                                    set.remove(start);
                                    set.remove(title);
                                    temp += "ɾ��" +name +"\r\n";
                                    continue;
                                }
                                set.add(desc);
                                set.add(title);
                                set.add(start);
                                if (file.equals(file1)) {
                                    set.remove(desc);
                                    set.remove(start);
                                }
                            }
//                            temp += file.getAbsolutePath() + "\n";
                        }

                        int i = 1;
                        for (String arr : set) {
                            if (arr.contains("     ")){
                                temp +="�� "+ i+ " ��"+arr + "\r\n";
                                i++;
                            }
                        }
//                        BufferedWriter bw = new BufferedWriter(new FileWriter(file1,true));
                        OutputStreamWriter bw = new OutputStreamWriter(new FileOutputStream(file1), "gb2312");

//                        List<String> collect = set.stream().filter(s -> !s.contains("     ")).collect(Collectors.toList());
                        List<String> collect = new ArrayList<>(set);
                        collect.add(0,"@echo off");
                        collect.add("pause");
                        for (String arr : collect) {
                            bw.write(arr + "\r\n");
                        }
                        bw.close();
                        JOptionPane.showMessageDialog(null, temp);
                        dtde.dropComplete(true);//ָʾ��ק���������

                    } else {
                        dtde.rejectDrop();//����ܾ���ק��������
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}