package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface pour valider une séance
 */
public class ValiderSeanceFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color successColor = new Color(46, 204, 113);   // Vert succès
    private Color dangerColor = new Color(231, 76, 60);     // Rouge danger
    
    // Composants de l'interface
    private JTextArea contenuArea;
    private JTextArea commentaireArea;
    private JButton validerButton, annulerButton;
    private JLabel statusLabel;
    
    // Données de la séance
    private int seanceId;
    private String coursNom;
    private int responsableId;
    
    // Référence à la fenêtre parente
    private VoirSeancesFrame parentFrame;

    public ValiderSeanceFrame(int seanceId, String coursNom, int responsableId, VoirSeancesFrame parentFrame) {
        this.seanceId = seanceId;
        this.coursNom = coursNom;
        this.responsableId = responsableId;
        this.parentFrame = parentFrame;
        
        // Configuration de la fenêtre
        setTitle("Valider la séance: " + coursNom);
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== FORMULAIRE ======
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // ====== BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Charger les détails de la séance
        chargerDetailsSeance();
        
        // Afficher la fenêtre
        setVisible(true);
    }
    
    /**
     * Crée le panneau d'entête avec le titre
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("VALIDATION DE SÉANCE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau du formulaire
     */
    private JPanel createFormPanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(backgroundColor);
        wrapperPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        // Titre du cours
        JLabel coursLabel = new JLabel("Cours: " + coursNom);
        coursLabel.setFont(new Font("Arial", Font.BOLD, 16));
        coursLabel.setForeground(primaryColor);
        coursLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Contenu de la séance
        JLabel contenuLabel = new JLabel("Contenu de la séance:");
        contenuLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        contenuArea = new JTextArea(8, 20);
        contenuArea.setEditable(false);
        contenuArea.setLineWrap(true);
        contenuArea.setWrapStyleWord(true);
        contenuArea.setFont(new Font("Arial", Font.PLAIN, 14));
        contenuArea.setBackground(new Color(245, 245, 245));
        contenuArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JScrollPane contenuScrollPane = new JScrollPane(contenuArea);
        contenuScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Commentaire de validation
        JLabel commentaireLabel = new JLabel("Commentaire (optionnel):");
        commentaireLabel.setFont(new Font("Arial", Font.BOLD, 14));
        commentaireLabel.setBorder(new EmptyBorder(15, 0, 5, 0));
        
        commentaireArea = new JTextArea(5, 20);
        commentaireArea.setLineWrap(true);
        commentaireArea.setWrapStyleWord(true);
        commentaireArea.setFont(new Font("Arial", Font.PLAIN, 14));
        commentaireArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        
        JScrollPane commentaireScrollPane = new JScrollPane(commentaireArea);
        commentaireScrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Message de statut
        statusLabel = new JLabel("");
        statusLabel.setForeground(dangerColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Ajouter les composants au panel
        formPanel.add(coursLabel);
        formPanel.add(contenuLabel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(contenuScrollPane);
        formPanel.add(commentaireLabel);
        formPanel.add(commentaireScrollPane);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(statusLabel);
        
        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        
        return wrapperPanel;
    }
    
    /**
     * Crée le panneau des boutons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Bouton Annuler
        annulerButton = new JButton("Annuler");
        annulerButton.setFont(new Font("Arial", Font.BOLD, 14));
        annulerButton.setPreferredSize(new Dimension(150, 40));
        annulerButton.setFocusPainted(false);
        annulerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bouton Valider
        validerButton = new JButton("Valider la séance");
        validerButton.setFont(new Font("Arial", Font.BOLD, 14));
        validerButton.setForeground(Color.WHITE);
        validerButton.setBackground(successColor);
        validerButton.setPreferredSize(new Dimension(180, 40));
        validerButton.setFocusPainted(false);
        validerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter les boutons au panel
        buttonPanel.add(annulerButton);
        buttonPanel.add(validerButton);
        
        // Actions des boutons
        annulerButton.addActionListener(e -> dispose());
        validerButton.addActionListener(e -> validerSeance());
        
        // Effet de survol
        validerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                validerButton.setBackground(new Color(39, 174, 96));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                validerButton.setBackground(successColor);
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les détails de la séance
     */
    private void chargerDetailsSeance() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Donnees.connexion_a_la_base();
            String sql = "SELECT contenu FROM seances WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, seanceId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                String contenu = rs.getString("contenu");
                contenuArea.setText(contenu);
                validerButton.setEnabled(true);
            } else {
                contenuArea.setText("Impossible de charger le contenu de la séance.");
                validerButton.setEnabled(false);
            }
        } catch (Exception e) {
            contenuArea.setText("Erreur lors du chargement du contenu: " + e.getMessage());
            validerButton.setEnabled(false);
            e.printStackTrace();
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
        }
    }
    
    /**
     * Valide la séance
     */
    private void validerSeance() {
        String commentaire = commentaireArea.getText().trim();
        
        // Validation dans la base de données
        boolean success = Donnees.validerSeance(seanceId, responsableId, commentaire);
        
        if (success) {
            // Mise à jour du statut
            statusLabel.setText("Séance validée avec succès !");
            statusLabel.setForeground(successColor);
            
            // Désactiver le bouton
            validerButton.setEnabled(false);
            
            // Rafraîchir la fenêtre parente
            if (parentFrame != null) {
                parentFrame.refreshSeances();
            }
            
            // Fermer après un délai
            Timer timer = new Timer(1500, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            statusLabel.setText("Erreur lors de la validation. Veuillez réessayer.");
            statusLabel.setForeground(dangerColor);
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        SwingUtilities.invokeLater(() -> new ValiderSeanceFrame(1, "Programmation Orientée Objet", 4, null));
    }
}