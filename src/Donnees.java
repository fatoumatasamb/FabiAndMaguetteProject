package src;
import java.sql.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Donnees {

    // Paramètres de connexion à la base de données
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CahierTexte";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    /**
     * Établit une connexion à la base de données
     * @return Connection - Objet de connexion à la base de données
     */
    public static Connection connexion_a_la_base() {
        Connection connection = null;
        try {
            // Chargement du driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver chargé avec succès");
            
            // Établir la connexion à la base de données
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Connexion à la base de données réussie");
        } catch (ClassNotFoundException e) {
            System.out.println("Erreur de chargement du driver: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Erreur de chargement du driver", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            System.out.println("Erreur de connexion à la base de données: " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Impossible de se connecter à la base de données", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
        return connection;
    }
    
    /**
     * Ferme proprement une connexion à la base de données
     * @param conn - La connexion à fermer
     */
    public static void fermerConnexion(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Connexion fermée avec succès");
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }
    
    /**
     * Ferme proprement un objet Statement
     * @param stmt - Le Statement à fermer
     */
    public static void fermerStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture du Statement: " + e.getMessage());
            }
        }
    }
    
    /**
     * Ferme proprement un objet ResultSet
     * @param rs - Le ResultSet à fermer
     */
    public static void fermerResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Erreur lors de la fermeture du ResultSet: " + e.getMessage());
            }
        }
    }
    
    /**
     * Authentifie un utilisateur dans le système
     * @param email - Email de l'utilisateur
     * @param motDePasse - Mot de passe de l'utilisateur
     * @return Map contenant les informations de l'utilisateur si authentification réussie, null sinon
     */
    public static Map<String, Object> authentifierUtilisateur(String email, String motDePasse) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Object> utilisateur = null;
        
        try {
            conn = connexion_a_la_base();
            String sql = "SELECT id, nom, email, role FROM users WHERE email = ? AND mot_de_passe = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                utilisateur = new HashMap<>();
                utilisateur.put("id", rs.getInt("id"));
                utilisateur.put("nom", rs.getString("nom"));
                utilisateur.put("email", rs.getString("email"));
                utilisateur.put("role", rs.getString("role"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'authentification: " + e.getMessage());
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return utilisateur;
    }
    
    /**
     * Récupère la liste des enseignants
     * @return ArrayList contenant les enseignants
     */
    public static ArrayList<Map<String, Object>> getEnseignants() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<Map<String, Object>> enseignants = new ArrayList<>();
        
        try {
            conn = connexion_a_la_base();
            stmt = conn.createStatement();
            String sql = "SELECT id, nom, email FROM users WHERE role = 'enseignant' ORDER BY nom";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> enseignant = new HashMap<>();
                enseignant.put("id", rs.getInt("id"));
                enseignant.put("nom", rs.getString("nom"));
                enseignant.put("email", rs.getString("email"));
                enseignants.add(enseignant);
            }
            
            // Affichage de débogage
            System.out.println("Nombre d'enseignants chargés: " + enseignants.size());
            for (Map<String, Object> e : enseignants) {
                System.out.println("Enseignant: " + e.get("nom") + ", ID: " + e.get("id"));
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des enseignants: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return enseignants;
    }
    
    
    /**
     * Récupère la liste des classes
     * @return ArrayList contenant les classes
     */
    public static ArrayList<Map<String, Object>> getClasses() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        ArrayList<Map<String, Object>> classes = new ArrayList<>();
        
        try {
            conn = connexion_a_la_base();
            stmt = conn.createStatement();
            String sql = "SELECT c.id, c.nom, c.responsable_id, u.nom as responsable_nom " +
                         "FROM classes c LEFT JOIN users u ON c.responsable_id = u.id";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                Map<String, Object> classe = new HashMap<>();
                classe.put("id", rs.getInt("id"));
                classe.put("nom", rs.getString("nom"));
                classe.put("responsable_id", rs.getObject("responsable_id"));
                classe.put("responsable_nom", rs.getString("responsable_nom"));
                classes.add(classe);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des classes: " + e.getMessage());
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return classes;
    }
    
    /**
     * Récupère la liste des cours d'un enseignant
     * @param enseignantId - ID de l'enseignant
     * @return ArrayList contenant les cours
     */
    public static ArrayList<Map<String, Object>> getCoursEnseignant(int enseignantId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Map<String, Object>> cours = new ArrayList<>();
        
        try {
            conn = connexion_a_la_base();
            String sql = "SELECT c.id, c.nom, cl.nom as classe_nom " +
                         "FROM cours c JOIN classes cl ON c.classe_id = cl.id " +
                         "WHERE c.enseignant_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, enseignantId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> unCours = new HashMap<>();
                unCours.put("id", rs.getInt("id"));
                unCours.put("nom", rs.getString("nom"));
                unCours.put("classe_nom", rs.getString("classe_nom"));
                cours.add(unCours);
            }
            
            // Affichage de débogage
            System.out.println("Nombre de cours chargés pour l'enseignant " + enseignantId + ": " + cours.size());
            for (Map<String, Object> c : cours) {
                System.out.println("Cours: " + c.get("nom") + ", ID: " + c.get("id") + ", Classe: " + c.get("classe_nom"));
            }
            
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des cours: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return cours;
    }
    
    /**
     * Récupère la liste des séances d'un cours
     * @param coursId - ID du cours
     * @return ArrayList contenant les séances
     */
    public static ArrayList<Map<String, Object>> getSeancesCours(int coursId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Map<String, Object>> seances = new ArrayList<>();
        
        try {
            conn = connexion_a_la_base();
            String sql = "SELECT id, date_seance, contenu, validee FROM seances WHERE cours_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coursId);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> seance = new HashMap<>();
                seance.put("id", rs.getInt("id"));
                seance.put("date", rs.getDate("date_seance"));
                seance.put("contenu", rs.getString("contenu"));
                seance.put("validee", rs.getBoolean("validee"));
                seances.add(seance);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des séances: " + e.getMessage());
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return seances;
    }
    
    /**
     * Ajoute un nouvel utilisateur (enseignant ou responsable)
     * @param nom - Nom complet de l'utilisateur
     * @param email - Email de l'utilisateur
     * @param motDePasse - Mot de passe de l'utilisateur
     * @param role - Rôle (enseignant ou responsable)
     * @return int - ID du nouvel utilisateur, -1 en cas d'erreur
     */
    public static int ajouterUtilisateur(String nom, String email, String motDePasse, String role) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int userId = -1;
        
        try {
            conn = connexion_a_la_base();
            String sql = "INSERT INTO users (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nom);
            stmt.setString(2, email);
            stmt.setString(3, motDePasse);
            stmt.setString(4, role);
            
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de l'utilisateur: " + e.getMessage());
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return userId;
    }
    
    /**
     * Ajoute un cours et l'assigne à un enseignant
     * @param nom - Nom du cours
     * @param enseignantId - ID de l'enseignant
     * @param classeId - ID de la classe
     * @return boolean - true si l'opération a réussi, false sinon
     */
    public static boolean ajouterCours(String nom, int enseignantId, int classeId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = connexion_a_la_base();
            String sql = "INSERT INTO cours (nom, enseignant_id, classe_id) VALUES (?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, nom);
            stmt.setInt(2, enseignantId);
            stmt.setInt(3, classeId);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout du cours: " + e.getMessage());
        } finally {
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return success;
    }
    
    /**
     * Ajoute une séance à un cours
     * @param coursId - ID du cours
     * @param date - Date de la séance
     * @param contenu - Contenu de la séance
     * @return boolean - true si l'opération a réussi, false sinon
     */
    public static boolean ajouterSeance(int coursId, java.sql.Date date, String contenu) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = connexion_a_la_base();
            String sql = "INSERT INTO seances (cours_id, date_seance, contenu, validee) VALUES (?, ?, ?, false)";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coursId);
            stmt.setDate(2, date);
            stmt.setString(3, contenu);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
        } catch (SQLException e) {
            System.out.println("Erreur lors de l'ajout de la séance: " + e.getMessage());
        } finally {
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return success;
    }
    
    /**
     * Valide une séance
     * @param seanceId - ID de la séance
     * @param responsableId - ID du responsable qui valide
     * @param commentaire - Commentaire optionnel
     * @return boolean - true si l'opération a réussi, false sinon
     */
    public static boolean validerSeance(int seanceId, int responsableId, String commentaire) {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtValidation = null;
        boolean success = false;
        
        try {
            conn = connexion_a_la_base();
            // Commencer une transaction
            conn.setAutoCommit(false);
            
            // Mettre à jour la séance (marquer comme validée)
            String sqlUpdate = "UPDATE seances SET validee = true WHERE id = ?";
            stmt = conn.prepareStatement(sqlUpdate);
            stmt.setInt(1, seanceId);
            stmt.executeUpdate();
            
            // Enregistrer la validation
            String sqlValidation = "INSERT INTO validations (seance_id, responsable_id, date_validation, commentaire) VALUES (?, ?, CURDATE(), ?)";
            stmtValidation = conn.prepareStatement(sqlValidation);
            stmtValidation.setInt(1, seanceId);
            stmtValidation.setInt(2, responsableId);
            stmtValidation.setString(3, commentaire);
            stmtValidation.executeUpdate();
            
            // Valider la transaction
            conn.commit();
            success = true;
        } catch (SQLException e) {
            System.out.println("Erreur lors de la validation de la séance: " + e.getMessage());
            try {
                // Annuler la transaction en cas d'erreur
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Erreur lors du rollback: " + ex.getMessage());
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                System.out.println("Erreur lors du rétablissement de l'autocommit: " + e.getMessage());
            }
            fermerStatement(stmt);
            fermerStatement(stmtValidation);
            fermerConnexion(conn);
        }
        
        return success;
    }
    
    /**
     * Récupère les séances à valider pour un responsable
     * @param responsableId - ID du responsable
     * @return ArrayList contenant les séances à valider
     */
/**
 * Récupère les séances à valider pour un responsable
 * @param responsableId - ID du responsable
 * @return ArrayList contenant les séances à valider
    */
    public static ArrayList<Map<String, Object>> getSeancesAValider(int responsableId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        ArrayList<Map<String, Object>> seances = new ArrayList<>();
        
        try {
            conn = connexion_a_la_base();
            
            // Trouver d'abord les classes dont ce responsable est en charge
            String sqlClasses = "SELECT id FROM classes WHERE responsable_id = ?";
            stmt = conn.prepareStatement(sqlClasses);
            stmt.setInt(1, responsableId);
            rs = stmt.executeQuery();
            
            // Collecter les IDs des classes
            ArrayList<Integer> classeIds = new ArrayList<>();
            while (rs.next()) {
                classeIds.add(rs.getInt("id"));
            }
            
            // Si aucune classe, retourner liste vide
            if (classeIds.isEmpty()) {
                return seances;
            }
            
            // Fermer le ResultSet et Statement précédents
            fermerResultSet(rs);
            fermerStatement(stmt);
            
            // Construire la clause IN pour les IDs de classes
            StringBuilder inClause = new StringBuilder();
            for (int i = 0; i < classeIds.size(); i++) {
                inClause.append("?");
                if (i < classeIds.size() - 1) {
                    inClause.append(",");
                }
            }
            
            // Requête pour obtenir les séances non validées des cours dans les classes du responsable
            String sql = "SELECT s.id, s.date_seance, s.contenu, c.nom as cours_nom, u.nom as enseignant_nom " +
                        "FROM seances s " +
                        "JOIN cours c ON s.cours_id = c.id " +
                        "JOIN users u ON c.enseignant_id = u.id " +
                        "WHERE c.classe_id IN (" + inClause.toString() + ") " +
                        "AND s.validee = false";
            
            stmt = conn.prepareStatement(sql);
            
            // Définir les paramètres pour la clause IN
            for (int i = 0; i < classeIds.size(); i++) {
                stmt.setInt(i + 1, classeIds.get(i));
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                Map<String, Object> seance = new HashMap<>();
                seance.put("id", rs.getInt("id"));
                seance.put("date", rs.getDate("date_seance"));
                seance.put("contenu", rs.getString("contenu"));
                seance.put("cours_nom", rs.getString("cours_nom"));
                seance.put("enseignant_nom", rs.getString("enseignant_nom"));
                seances.add(seance);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des séances à valider: " + e.getMessage());
            e.printStackTrace();
        } finally {
            fermerResultSet(rs);
            fermerStatement(stmt);
            fermerConnexion(conn);
        }
        
        return seances;
    }
}