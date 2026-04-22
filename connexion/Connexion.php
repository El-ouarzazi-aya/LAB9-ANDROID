<?php
class Connexion {
    private $connexion;

    public function __construct() {
        try {
            $this->connexion = new PDO(
                "mysql:host=127.0.0.1;port=3307;dbname=school1;charset=utf8",
                "root", ""
            );
            $this->connexion->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
            $this->connexion->setAttribute(PDO::ATTR_DEFAULT_FETCH_MODE, PDO::FETCH_ASSOC);
        } catch (PDOException $e) {
            http_response_code(500);
            die(json_encode(["erreur" => "Connexion échouée : " . $e->getMessage()]));
        }
    }

    public function getConnexion() { return $this->connexion; }
}
?>