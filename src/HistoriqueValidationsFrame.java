package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Interface pour visualiser l'historique des validations
 */
public class HistoriqueValidationsFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    
    // Composants de l'interface
    private JTable historiqueTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> filtreCoursComboBox;
    private JButton appliquerFiltreButton;
    private JButton reinitialiserFiltreButton;
    private JButton fermerButton;
    
    // ID du responsable
    private int responsableId;
    
    // Stockage des IDs de cours
    private ArrayList<Integer> coursIds = new ArrayList<>();

    public HistoriqueValidationsFrame(int responsableId) {
        this.responsableId = responsableId;
        
        // Configuration de la fenêtre
        setTitle("Historique des Validations");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== PANEL DE FILTRE ======
        JPanel filtrePanel = createFiltrePanel();
        add(filtrePanel, BorderLayout.NORTH);
        
        // ====== TABLEAU DE L'HISTORIQUE ======
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // ====== PANEL DES BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Charger tous les cours assignés au responsable
        chargerCours();
        
        // Charger l'historique complet par défaut
        chargerHistorique(0); // 0 = tous les cours
        
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
        
        JLabel titleLabel = new JLabel("HISTORIQUE DES VALIDATIONS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau de filtre pour les cours
     */
    private JPanel createFiltrePanel() {
        JPanel filtrePanel = new JPanel();
        filtrePanel.setBackground(new Color(245, 245, 245));
        filtrePanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel filtreLabel = new JLabel("Filtrer par cours:");
        filtreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        filtreCoursComboBox = new JComboBox<>();
        filtreCoursComboBox.setPreferredSize(new Dimension(300, 30));
        
        appliquerFiltreButton = new JButton("Appliquer");
        appliquerFiltreButton.setFont(new Font("Arial", Font.PLAIN, 14));
        appliquerFiltreButton.setBackground(primaryColor);
        appliquerFiltreButton.setForeground(Color.WHITE);
        appliquerFiltreButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        reinitialiserFiltreButton = new JButton("Tout afficher");
        reinitialiserFiltreButton.setFont(new Font("Arial", Font.PLAIN, 14));
        reinitialiserFiltreButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Actions des boutons
        appliquerFiltreButton.addActionListener(e -> {
            int selectedIndex = filtreCoursComboBox.getSelectedIndex();
            if (selectedIndex > 0) { // Si une option autre que "Tous les cours" est sélectionnée
                chargerHistorique(coursIds.get(selectedIndex));
            } else {
                chargerHistorique(0); // 0 = tous les cours
            }
        });
        
        reinitialiserFiltreButton.addActionListener(e -> {
            filtreCoursComboBox.setSelectedIndex(0);
            chargerHistorique(0);
        });
        
        // Ajouter les composants au panel
        filtrePanel.add(filtreLabel);
        filtrePanel.add(Box.createHorizontalStrut(10));
        filtrePanel.add(filtreCoursComboBox);
        filtrePanel.add(Box.createHorizontalStrut(10));
        filtrePanel.add(appliquerFiltreButton);
        filtrePanel.add(Box.createHorizontalStrut(5));
        filtrePanel.add(reinitialiserFiltreButton);
        
        return filtrePanel;
    }
    
    /**
     * Crée le panneau du tableau d'historique
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(0, 20, 20, 20));
        
        // En-tête du tableau
        String[] columns = {"Cours", "Date Validation", "Date Séance", "Commentaire"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Tableau non éditable
            }
        };
        
        // Création du tableau
        historiqueTable = new JTable(tableModel);
        historiqueTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        historiqueTable.setRowHeight(30);
        historiqueTable.setFont(new Font("Arial", Font.PLAIN, 14));
        historiqueTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        historiqueTable.getTableHeader().setBackground(new Color(220, 220, 220));
        
        // Configurer le tri
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        historiqueTable.setRowSorter(sorter);
        
        // Ajouter le tableau à un ScrollPane
        JScrollPane scrollPane = new JScrollPane(historiqueTable);
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
        
        // Bouton Fermer
        fermerButton = new JButton("Fermer");
        fermerButton.setFont(new Font("Arial", Font.BOLD, 14));
        fermerButton.setPreferredSize(new Dimension(150, 40));
        fermerButton.setFocusPainted(false);
        fermerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter les boutons au panel
        buttonPanel.add(fermerButton);
        
        // Actions des boutons
        fermerButton.addActionListener(e -> dispose());
        
        return buttonPanel;
    }
    
    /**
     * Charge les cours assignés au responsable dans la combobox
     */
    private void chargerCours() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Donnees.connexion_a_la_base();
            
            // Trouver toutes les classes dont ce responsable est en charge
            String sql1 = "SELECT id FROM classes WHERE responsable_id = ?";
            stmt = conn.prepareStatement(sql1);
            stmt.setInt(1, responsableId);
            rs = stmt.executeQuery();
            
            ArrayList<Integer> classeIds = new ArrayList<>();
            while (rs.next()) {
                classeIds.add(rs.getInt("id"));
            }
            
            // Fermer les ressources de la première requête
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            
            // Si aucune classe n'est assignée, sortir
            if (classeIds.isEmpty()) {
                filtreCoursComboBox.addItem("Pas de classes assignées");
                return;
            }
            
            // Construire la requête IN pour les classes
            StringBuilder inClause = new StringBuilder();
            for (int i = 0; i < classeIds.size(); i++) {
                inClause.append("?");
                if (i < classeIds.size() - 1) {
                    inClause.append(",");
                }
            }
            
            // Récupérer tous les cours pour ces classes
            String sql2 = "SELECT id, nom FROM cours WHERE classe_id IN (" + inClause.toString() + ")";
            stmt = conn.prepareStatement(sql2);
            
            // Définir les paramètres de la requête
            for (int i = 0; i < classeIds.size(); i++) {
                stmt.setInt(i + 1, classeIds.get(i));
            }
            
            rs = stmt.executeQuery();
            
            // Initialiser les listes
            filtreCoursComboBox.removeAllItems();
            coursIds.clear();
            
            // Ajouter l'option "Tous les cours"
            filtreCoursComboBox.addItem("Tous les cours");
            coursIds.add(0);
            
            // Ajouter chaque cours
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                filtreCoursComboBox.addItem(nom);
                coursIds.add(id);
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des cours");
            e.printStackTrace();
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
        }
    }
    
    /**
     * Charge l'historique des validations
     * @param coursId - ID du cours pour filtrer, 0 pour tous les cours
     */
    private void chargerHistorique(int coursId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Donnees.connexion_a_la_base();
            
            // Construire la requête SQL en fonction du filtre
            String sql;
            if (coursId > 0) {
                // Filtrer par cours spécifique
                sql = "SELECT c.nom AS cours_nom, v.date_validation, s.date_seance, v.commentaire " +
                      "FROM validations v " +
                      "JOIN seances s ON v.seance_id = s.id " +
                      "JOIN cours c ON s.cours_id = c.id " +
                      "JOIN classes cl ON c.classe_id = cl.id " +
                      "WHERE v.responsable_id = ? AND s.cours_id = ? " +
                      "ORDER BY v.date_validation DESC";
                
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, responsableId);
                stmt.setInt(2, coursId);
            } else {
                // Afficher toutes les validations pour les classes sous la responsabilité
                sql = "SELECT c.nom AS cours_nom, v.date_validation, s.date_seance, v.commentaire " +
                      "FROM validations v " +
                      "JOIN seances s ON v.seance_id = s.id " +
                      "JOIN cours c ON s.cours_id = c.id " +
                      "JOIN classes cl ON c.classe_id = cl.id " +
                      "WHERE v.responsable_id = ? " +
                      "ORDER BY v.date_validation DESC";
                
                stmt = conn.prepareStatement(sql);
                stmt.setInt(1, responsableId);
            }
            
            rs = stmt.executeQuery();
            
            // Nettoyer le tableau
            tableModel.setRowCount(0);
            
            // Remplir le tableau avec les validations
            while (rs.next()) {
                String coursNom = rs.getString("cours_nom");
                java.sql.Date dateValidation = rs.getDate("date_validation");
                java.sql.Date dateSeance = rs.getDate("date_seance");
                String commentaire = rs.getString("commentaire");
                
                if (commentaire == null) {
                    commentaire = ""; // Éviter les valeurs nulles
                }
                
                Object[] row = {
                    coursNom,
                    dateValidation.toString(),
                    dateSeance.toString(),
                    commentaire
                };
                tableModel.addRow(row);
            }
            
            // Afficher un message si aucune validation n'est trouvée
            if (tableModel.getRowCount() == 0) {
                if (coursId > 0) {
                    // Message pour un cours spécifique
                    JOptionPane.showMessageDialog(
                        this,
                        "Aucune validation trouvée pour ce cours.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                } else {
                    // Message général
                    JOptionPane.showMessageDialog(
                        this,
                        "Aucune validation n'a encore été effectuée.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement de l'historique");
            e.printStackTrace();
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
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
        
        SwingUtilities.invokeLater(() -> new HistoriqueValidationsFrame(4));
    }
}