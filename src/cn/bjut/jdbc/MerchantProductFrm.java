package cn.bjut.jdbc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MerchantProductFrm extends JPanel {

    private MerchantInterFrm merchantInterFrm;
    private DataControlMercahnt dataControlmer = new DataControlMercahnt();
    private ProductDetailsDialog currentDetailsDialog;
    public JPanel productsPanel;

    public MerchantProductFrm(MerchantInterFrm mer) throws SQLException {
        this.merchantInterFrm = mer;
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 2));
        createProductCard();
        add(productsPanel,BorderLayout.NORTH);
    }
    public void createProductCard() {
        try {
            List<Product> products = dataControlmer.MerchantProductQuery(merchantInterFrm.getM_id());
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                productsPanel.add(productPanel);
            }
            revalidate();
            repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel(new BorderLayout());
        productPanel.putClientProperty("product", product);

        JLabel imageLabel = createImageLabel(product, 300, 280);
        productPanel.add(imageLabel, BorderLayout.WEST);

        JPanel infoPanel = createInfoPanel(product);
        productPanel.add(infoPanel, BorderLayout.CENTER);

        return productPanel;
    }

    private JPanel createInfoPanel(Product product) {
        JPanel infoPanel = new JPanel(new GridLayout(0, 1));
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        JLabel nameLabel = createLabel("商品名称: " + product.getP_name());
        JLabel priceLabel = createLabel("商品价格: " + product.getP_price() + "元");

        String statusColor = getStatusColor(product.getP_status());
        JLabel statusLabel = createLabel("<html>商品状态: " + statusColor + "</html>");

        JLabel quantityLabel = createLabel("商品数量: " + product.getP_quantity());

        infoPanel.add(nameLabel);
        infoPanel.add(priceLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(quantityLabel);

        return infoPanel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, 18));
        return label;
    }

    private String getStatusColor(String status) {
        if ("上架".equals(status)) {
            return "<font color='green'>上架</font>";
        } else if ("下架".equals(status)) {
            return "<font color='red'>下架</font>";
        }
        return "";
    }

    // 在类的成员变量中添加一个缓存Map
    private Map<String, ImageIcon> imageCache = new HashMap<>();

    // 优化后的 createImageLabel 方法
    public JLabel createImageLabel(Product product, int width, int height) {
        ImageIcon placeHolderIcon = new ImageIcon(getClass().getResource("/Img/loadphoto.jpg"));
        JLabel imageLabel = new JLabel(placeHolderIcon);

        SwingWorker<ImageIcon, Void> worker = new SwingWorker() {
            @Override
            protected ImageIcon doInBackground() {
                return getScaledImageIcon(product, width, height);
            }

            @Override
            protected void done() {
                try {
                    ImageIcon scaledIcon = (ImageIcon) get();
                    imageLabel.setIcon(scaledIcon);
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    showProductDetails(product);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return imageLabel;
    }

    // 优化后的 getScaledImageIcon 方法，添加了图片缓存
    private ImageIcon getScaledImageIcon(Product product, int width, int height) {
        String imagePath = product.getP_img();
        ImageIcon cachedIcon = imageCache.get(imagePath);

        if (cachedIcon != null) {
            return cachedIcon;
        }

        ImageIcon originalIcon = getImageIcon(product);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // 将加载过的图片放入缓存
        imageCache.put(imagePath, scaledIcon);

        return scaledIcon;
    }


    private ImageIcon getImageIcon(Product product) {
        String projectPath = System.getProperty("user.dir");
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;

        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            return new ImageIcon(absoluteImagePath);
        } else {
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            return new ImageIcon(defaultImagePath);
        }
    }

    private void showProductDetails(Product product) throws SQLException {
        if (currentDetailsDialog != null) {
            currentDetailsDialog.dispose();
        }

        currentDetailsDialog = new ProductDetailsDialog(product, merchantInterFrm,this);
        currentDetailsDialog.setVisible(true);
    }


    public void refreshCard1Product(Product updatedProduct) {
        int index = findProductIndex(updatedProduct);
        if (index >= 0) {
            productsPanel.remove(index);
            JPanel productPanel = createProductPanel(updatedProduct);
            productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            productsPanel.add(productPanel, index);
            productsPanel.revalidate();
            productsPanel.repaint();
        }
    }

    private int findProductIndex(Product updatedProduct) {
        Component[] components = getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JPanel) {
                JPanel productPanel = (JPanel) components[i];
                Product product = (Product) productPanel.getClientProperty("product");
                if (product != null && product.getP_id() == updatedProduct.getP_id()) {
                    return i;
                }
            }
        }
        return -1;
    }
}
