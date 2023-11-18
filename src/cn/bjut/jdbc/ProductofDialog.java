package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

//商品界面父类
public class ProductofDialog extends JDialog {
    public DataControlProduct dataControlProduct = new DataControlProduct();
    public Product product;
    public String newImgName = "R.jpg";
    protected JLabel imageLabel; // 修改这一行
    public JRadioButton onSaleRadioButton;
    public JRadioButton offSaleRadioButton;
    public GridBagConstraints gbc = new GridBagConstraints();
    public JPanel panel = new JPanel(new GridBagLayout());

    public ProductofDialog(Product product) throws SQLException {
        this.product = product;
        initComponents();
    }

    private void initComponents() {
        // 创建微软雅黑16号字体
        Font customFont = new Font("微软雅黑", Font.BOLD, 22);

        setSize(900, 700);
        setLocationRelativeTo(null); // 居中显示

        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel name = new JLabel("商品名称:    ");
        name.setFont(customFont);
        panel.add(name, gbc);

        gbc.gridy++;

        // 添加商品描述标签，应用字体
        JLabel descLabel = new JLabel("商品描述:    ");
        descLabel.setFont(customFont);
        panel.add(descLabel, gbc);
        gbc.gridy++;

        // 添加商品类别标签，应用字体
        JLabel classLabel = new JLabel("类别:   ");
        classLabel.setFont(customFont);
        panel.add(classLabel, gbc);
        gbc.gridy++;

        // 添加商品价格标签，应用字体
        JLabel priceLabel = new JLabel("单价:   ");
        priceLabel.setFont(customFont);
        panel.add(priceLabel, gbc);
        gbc.gridy++;

        // 添加商品数量标签，应用字体
        JLabel quantityLabel = new JLabel("剩余数量:    ");
        quantityLabel.setFont(customFont);
        panel.add(quantityLabel, gbc);
        gbc.gridy++;

        // 添加商品状态标签，应用字体
        JLabel statusLabel = new JLabel("商品状态:    ");
        statusLabel.setFont(customFont);
        panel.add(statusLabel, gbc);
        gbc.gridy++;


        gbc.gridx = 1;
        add(panel);
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
        Image scaledImage = updatedImage.getScaledInstance(250, 200, Image.SCALE_SMOOTH);
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
        success = dataControlProduct.updateProduct(product.getP_id(), newName, newDesc, newClass, newPrice, newStatus, newquantity, newImgName);
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

                ImageIcon updatedIcon = getImageIcon(newImgName);
                imageLabel.setIcon(updatedIcon);

                imageLabel.getParent().revalidate();
                imageLabel.getParent().repaint();
                return true;
            }
        }
        return false;
    }
}

