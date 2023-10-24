package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class ProductofDialog extends JDialog{
    public DataControl dataControl;
    public Product product;
    public String newImgName; // 添加字段用于存储文件名

    // 在initComponents方法中添加一个新的字段来保存商品图片的标签
    public JLabel imageLabel;
    public JRadioButton onSaleRadioButton;
    public JRadioButton offSaleRadioButton;
    public GridBagConstraints gbc = new GridBagConstraints();
    public JPanel panel = new JPanel(new GridBagLayout());

    public ProductofDialog(DataControl dataControl, Product product) {
        this.dataControl = dataControl;
        this.product = product;
        this.newImgName = product.getP_img();
        initComponents();
    }

    private void initComponents() {
        setSize(900, 600);
        setLocationRelativeTo(null); // 居中显示

        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JLabel("商品名称:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("商品描述:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("商品类别:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("商品价格（元）:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("商品数量:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("商品状态:"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("商品图片:"), gbc);
        gbc.gridx = 1;

        getContentPane().add(panel, BorderLayout.CENTER);
    }

    //刷新修改界面的图片
    public void refreshphoto() {
        // 获取项目路径
        String projectPath = System.getProperty("user.dir");
        // 构建新图片路径
        ImageIcon updatedIcon = getImageIcon(projectPath);
        // 获取图片对象
        Image updatedImage = updatedIcon.getImage();
        // 缩放图片（如果需要）
        Image scaledImage = updatedImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
        // 创建一个新的 ImageIcon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        // 更新图片标签
        imageLabel.setIcon(scaledIcon);
    }

    public ImageIcon getImageIcon(String projectPath) {
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + newImgName;
        // 创建新的 ImageIcon
        ImageIcon updatedIcon;
        File updatedImageFile = new File(absoluteImagePath);
        if (updatedImageFile.exists()) {
            updatedIcon = new ImageIcon(absoluteImagePath);
        } else {
            // 图片路径不存在，使用默认图片
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            updatedIcon = new ImageIcon(defaultImagePath);
        }
        return updatedIcon;
    }

    //复制图片文件
    public boolean FilephotoCopy(String newImgPath, String newImgName) {
        String currentDirectory = System.getProperty("user.dir");
        String destinationPath = currentDirectory + "\\src\\Img\\" + newImgName;
        File sourceFile = new File(newImgPath);
        File destinationFile = new File(destinationPath);
        try {
            Path source = sourceFile.toPath();
            Path destination = destinationFile.toPath();
            // 使用Files.copy方法复制文件
            Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("文件复制成功");
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("文件复制失败");
            return false;
        }
    }

    // 用于处理“Update”按钮功能的方法
    protected boolean handleUpdateButton(String newName, String newDesc, String newClass, double newPrice, String newStatus, int newquantity) {
        boolean success;
        success = dataControl.updateProduct(product.getP_id(), newName, newDesc, newClass, newPrice, newStatus, newquantity, newImgName);
        return success;
    }

    // 用于处理“Change Image”按钮功能的方法
    protected boolean handleChangeImageButton() {
        JFileChooser fileChooser = new JFileChooser();
        int returnVal = fileChooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            String newImgPath = fileChooser.getSelectedFile().getPath();
            newImgName = new File(newImgPath).getName();
            boolean b = FilephotoCopy(newImgPath, newImgName);

            if (b) {
                // Update the image label with the new image
                ImageIcon updatedIcon = getImageIcon(newImgName);
                imageLabel.setIcon(updatedIcon);
                // Revalidate and repaint the container to reflect the changes
                imageLabel.getParent().revalidate();
                imageLabel.getParent().repaint();
                return true;
            }
        }
        return false;
    }
}

