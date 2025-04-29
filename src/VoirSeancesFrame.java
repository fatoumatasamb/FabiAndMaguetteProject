package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface pour visualiser les séances à valider par le responsable
 */
public class VoirSeancesFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    
    // Composants de l'interface
    private JTable seancesTable;
    private DefaultTableModel tableModel;
    private JButton validerButton, fermerButton;
    
    // ID du responsable
    private int responsableId;

    public VoirSeancesFrame(int responsableId) {
        this.responsableId = responsableId;
        
        // Configuration de la fenêtre
        setTitle("Séances à valider");
        setSize(900, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== TABLEAU DES SÉANCES ======
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // ====== PANEL DES BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Charger les séances
        chargerSeances();
        
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
        
        JLabel titleLabel = new JLabel("SÉANCES À VALIDER");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau du tableau des séances
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // En-tête du tableau
        String[] columns = {"ID", "Cours", "Enseignant", "Date", "Contenu"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Tableau non éditable
            }
        };
        
        // Création du tableau
        seancesTable = new JTable(tableModel);
        seancesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seancesTable.setRowHeight(30);
        seancesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        seancesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        seancesTable.getTableHeader().setBackground(new Color(220, 220, 220));
        seancesTable.getTableHeader().setForeground(new Color(50, 50, 50));
        
        // Masquer la colonne ID
        seancesTable.getColumnModel().getColumn(0).setMinWidth(0);
        seancesTable.getColumnModel().getColumn(0).setMaxWidth(0);
        seancesTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Ajuster les largeurs des colonnes
        seancesTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Cours
        seancesTable.getColumnModel().getColumn(2).setPreferredWidth(200);  // Enseignant
        seancesTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Date
        seancesTable.getColumnModel().getColumn(4).setPreferredWidth(400);  // Contenu
        
        // Ajouter le tableau à un ScrollPane
        JScrollPane scrollPane = new JScrollPane(seancesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Ajouter le ScrollPane au panel
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Crée le panneau des boutons d'action
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Bouton Valider
        validerButton = new JButton("Valider la séance");
        validerButton.setFont(new Font("Arial", Font.BOLD, 14));
        validerButton.setForeground(Color.WHITE);
        validerButton.setBackground(primaryColor);
        validerButton.setPreferredSize(new Dimension(180, 40));
        validerButton.setFocusPainted(false);
        validerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bouton Fermer
        fermerButton = new JButton("Fermer");
        fermerButton.setFont(new Font("Arial", Font.BOLD, 14));
        fermerButton.setPreferredSize(new Dimension(150, 40));
        fermerButton.setFocusPainted(false);
        fermerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter les boutons au panel
        buttonPanel.add(validerButton);
        buttonPanel.add(fermerButton);
        
        // Actions des boutons
        validerButton.addActionListener(e -> validerSeance());
        fermerButton.addActionListener(e -> dispose());
        
        // Effet de survol
        validerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                validerButton.setBackground(new Color(0, 90, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                validerButton.setBackground(primaryColor);
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les séances à valider pour le responsable
     */
    private void chargerSeances() {
        // Nettoyer le tableau
        tableModel.setRowCount(0);
        
        // Récupérer les séances à valider
        ArrayList<Map<String, Object>> seances = Donnees.getSeancesAValider(responsableId);
        
        // Remplir le tableau avec les séances
        for (Map<String, Object> seance : seances) {
            Object[] row = {
                seance.get("id"),
                seance.get("cours_nom"),
                seance.get("enseignant_nom"),
                ((java.sql.Date) seance.get("date")).toString(),
                seance.get("contenu")
            };
            tableModel.addRow(row);
        }
        
        // Activer/désactiver le bouton de validation
        validerButton.setEnabled(tableModel.getRowCount() > 0);
        
        // Message si aucune séance n'est disponible
        if (seances.isEmpty()) {
            JLabel messageLabel = new JLabel("Aucune séance en attente de validation.");
            messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setForeground(new Color(100, 100, 100));
            
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.add(messageLabel, BorderLayout.CENTER);
            
            JScrollPane scrollPane = (JScrollPane) seancesTable.getParent().getParent();
            scrollPane.setViewportView(emptyPanel);
        }
    }
    
    /**
     * Valide la séance sélectionnée
     */
    private void validerSeance() {
        int selectedRow = seancesTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Veuillez sélectionner une séance à valider",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Récupérer l'ID de la séance sélectionnée
        int seanceId = (int) tableModel.getValueAt(selectedRow, 0);
        String coursNom = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Ouvrir la fenêtre de validation
        new ValiderSeanceFrame(seanceId, coursNom, responsableId, this);
    }
    
    /**
     * Rafraîchit la liste des séances (appelé après validation)
     */
    public void refreshSeances() {
        chargerSeances();
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
        
        SwingUtilities.invokeLater(() -> new VoirSeancesFrame(4));
    }
}