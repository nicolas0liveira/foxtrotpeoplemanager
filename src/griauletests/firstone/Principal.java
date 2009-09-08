/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package griauletests.firstone;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.griaule.grfingerjava.FingerprintImage;
import com.griaule.grfingerjava.GrFingerJava;
import com.griaule.grfingerjava.GrFingerJavaException;
import com.griaule.grfingerjava.IFingerEventListener;
import com.griaule.grfingerjava.IImageEventListener;
import com.griaule.grfingerjava.IStatusEventListener;
import com.griaule.grfingerjava.MatchingContext;
import com.griaule.grfingerjava.Template;
import com.mysql.jdbc.Connection;


public class Principal implements IStatusEventListener, IImageEventListener,
		IFingerEventListener {

	private FrmPrincipal ui;
	private FingerprintImage img_impressaodigital;
	private Template template;
	private MatchingContext fingerprintSDK;

	// MEU BD
	private Connection dbCon;
	// Comando sql para inserir no banco*/
	private PreparedStatement enrollStmt;
	// Retornar o ultimo id usado*/
	private PreparedStatement insertedIdStmt;
	// Retornar todos os templates do BD
	private PreparedStatement identifyStmt;
	// Passo um ID e o db me retorna o template
	private PreparedStatement verifyStmt;
	// LIMPAR DB
	private PreparedStatement clearDbStmt;

	private PreparedStatement getSensor;

	public Principal() {

		// inicializando variaveis
		ui = new FrmPrincipal();
		ui.setVisible(true);
		// dizendo a dll da griaule onte esta a licenca para uso...

		// Era s� isso....
		String grFingerNativeDirectory = System.getProperty("user.dir");

		grFingerNativeDirectory = grFingerNativeDirectory
				+ "/libs/Griaule/Fingerprint SDK Java 2009/bin/";

		System.out.println(grFingerNativeDirectory);

		setFingerprintSDKNativeDirectory(grFingerNativeDirectory);
		ui.add_lineInLog("End. da licensa griaule utilizada: "
				+ grFingerNativeDirectory);
		// fim da definiçao da licença!

		// inicializando conexao com o BD
		this.initDB();

		// inicializando
		initFingerprintSDK();

	}

	// config DB
	private void initDB() {
		String servidor = "200.234.202.125";
		String bd = "csji2";
		String user = "csji2";
		String senha = "fox**bd";
		try {
			// ODBC driver.
			Class.forName("com.mysql.jdbc.Driver");

			// connectar a um BD
			dbCon = (Connection) DriverManager.getConnection("jdbc:mysql://"
					+ servidor + "/" + bd, user, senha);

			// Preparedestatments que seram utilizados para inserções
			enrollStmt = dbCon
					.prepareStatement("INSERT INTO teste_impressoes(template, id_mao, id_dedo, nome) values(?, ?, ?, ?)");
			insertedIdStmt = dbCon
					.prepareStatement("SELECT MAX(ID) FROM teste_impressoes");
			identifyStmt = dbCon
					.prepareStatement("SELECT * FROM teste_impressoes");
			clearDbStmt = dbCon
					.prepareStatement("DELETE FROM teste_impressoes");
			verifyStmt = dbCon
					.prepareStatement("SELECT template FROM teste_impressoes WHERE ID=?");

			// Preparedestatments que seram utilizados pelo sensor
			getSensor = dbCon
					.prepareStatement("SELECT * FROM sensores WHERE Id =?");

			ui.add_lineInLog(">>Conectado com sucesso em " + servidor
					+ " com o login " + user);

		} catch (Exception e) {
			e.printStackTrace();
			ui.add_lineInLog("Não foi possivel conectar em " + servidor
					+ " com o login " + user);
			System.out.println(e);
		}

	}

	public static void setFingerprintSDKNativeDirectory(String nativeDirectory) {
		File directory = new File(nativeDirectory);

		try {
			GrFingerJava.setNativeLibrariesDirectory(directory);
			GrFingerJava.setLicenseDirectory(directory);
		} catch (GrFingerJavaException e) {
			e.printStackTrace();
		}
	}

	private void initFingerprintSDK() {
		int c = 15;
		ui.set_barra(c);
		try {
			fingerprintSDK = new MatchingContext();
			// Starts fingerprint capture.
			GrFingerJava.initializeCapture(this);

			ui.add_lineInLog(">>SDK inicializado com sucesso!!");

		} catch (Exception e) {
			// If any error ocurred while initializing GrFinger,
			// writes the error to log
			ui.add_lineInLog(e.getMessage());
		}
		// Aqui jaz o sonho de fazer uma barra de loading...
		while (c < 100) {
			c++;
			ui.set_barra(c);
		}

	}

	// classe que termina o programa
	public void destruir() {
		try {
			GrFingerJava.finalizeCapture();
			ui.add_lineInLog(">>Finalizado");

		} catch (GrFingerJavaException ex) {
			ui
					.add_lineInLog(">>Não foi possível finalizar a operação de saida");
		}

	}

	public void onSensorPlug(String id_sensor) {

		ui.add_linetosenrorlog("Novo sensor detectado... Id do sensor: "
				+ id_sensor);
		ui.add_linetosenrorlog("Tentando inicializar captura com o sensor: "
				+ id_sensor);

		try {
			GrFingerJava.startCapture(id_sensor, this, this);
			ui.add_linetosenrorlog("OK!! \n Aguardando captura no sensor...");
		} catch (GrFingerJavaException ex) {
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		initDB();
		/*
		 * ui.add_linetosenrorlog("Localizando sensor no DB");
		 * 
		 * try { getSensor.setString(1, id_sensor); ResultSet rs =
		 * getSensor.executeQuery(); // terminar de receber consulta e exibir se
		 * o id do sensor ja existe } catch (SQLException ex) {
		 * Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null,
		 * ex); } // ResultSet rs =
		 */
	}

	// Sensor removido
	public void onSensorUnplug(String id_sensor) {
		ui.add_lineInLog("O sensor \"" + id_sensor + "\" foi removido");
		initDB();

	}

	public void onImageAcquired(String id_sensor, FingerprintImage img_sensor) {
		try {
			ui.add_lineInLog("O sensor \"" + id_sensor
					+ "\" capturou uma imagem:" + img_sensor);
			this.img_impressaodigital = img_sensor;
			// extraindo template dessa nova imagem...
			extrair_template();

			// se ainda não foi identificada... salve no BD! dependendo das
			// opcoes marcadas no form
			if (ui.get_podeverificar()) {
				if (!identify()) {

					if (ui.get_podesalvar()) {
						this.enroll();
					}
				}
			}
			if (ui.get_vertemplate()) {
				ui.set_imagem(GrFingerJava.getBiometricImage(template,
						img_impressaodigital));
			} else {
				ui.set_imagem(img_impressaodigital);
			}
		} catch (GrFingerJavaException ex) {
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (IllegalArgumentException ex) {
			Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null,
					ex);
		}

	}

	public void extrair_template() {

		try {
			// Extracts a template from the current fingerprint image.
			this.template = fingerprintSDK.extract(img_impressaodigital);

			// Notifies it has been extracted and the quality of the extraction
			String msg = "Um template foi extraido com  ";
			// write template quality to log
			switch (template.getQuality()) {
			case Template.HIGH_QUALITY:
				msg += "Qualidade alta.";
				break;
			case Template.MEDIUM_QUALITY:
				msg += "Qualidade média.";
				break;
			case Template.LOW_QUALITY:
				msg += "Qualidade baixa.";
				break;
			}
			ui.add_lineInLog(msg);

		} catch (GrFingerJavaException e) {
			// write error to log
			ui.add_lineInLog(e.getMessage());
		}

	}

	public void enroll() {
		try {
			// Salva template no DB
			enrollStmt.setBinaryStream(1, new ByteArrayInputStream(template
					.getData()), template.getData().length);
			enrollStmt.setInt(2, ui.get_mao());
			enrollStmt.setInt(3, ui.get_dedo());
			enrollStmt.setString(4, ui.get_nome());
			enrollStmt.executeUpdate();

			// Qual ID foi associado ao template que foi extraido
			ResultSet rs = insertedIdStmt.executeQuery();
			rs.next();
			ui.add_lineInLog("Nova impressão salva no BD com ID = "
					+ Integer.toString(rs.getInt(1)) + " Nome: "
					+ ui.get_nome());

		} catch (SQLException e) {
			System.out.println(e);
			ui.add_lineInLog("Não foi possível salvar no BD");
		}
	}

	public boolean identify() {
		try {
			// Starts identification process by supplying query template.
			fingerprintSDK.prepareForIdentification(this.template);

			// Gets enrolled templates from database.
			ResultSet rs = identifyStmt.executeQuery();

			// Iterate over all templates in database
			while (rs.next()) {
				// Reads the current template data on a buffer
				byte[] templateBuffer = rs.getBytes("template");
				// And creates a new Template
				Template referenceTemplate = new Template(templateBuffer);

				// Compares current template.
				boolean matched = fingerprintSDK.identify(referenceTemplate);

				// If the templates match, display matching
				// minutiae/segments/directions.
				if (matched) {
					// displays minutiae/segments/directions that matched.
					// Notifies the template was identified.
					ui
							.add_lineInLog("Impressão identificada! Score de identificação = "
									+ fingerprintSDK.getScore()
									+ " ID = "
									+ rs.getInt("ID")
									+ " Nome: "
									+ rs.getString("nome"));

					ui.updateform(rs.getInt("id_mao"), rs.getInt("id_dedo"));
					ui.set_nome(rs.getString("nome"));

					ui.set_imagemcomgrafo(GrFingerJava.getBiometricImage(
							template, img_impressaodigital, fingerprintSDK));

					rs.close();
					// Stops searching
					return true;
				}
			}

			// If all templates on the DB have been compared, and none of them
			// match, notifies it has not been found.
			ui.add_lineInLog("Impressão não localizada no BD");
			ui.set_imagemcomgrafo(null);
			ui.limpaForm();
			rs.close();
			return false;

		} catch (SQLException e) {
			e.printStackTrace();
			// write error to log
			ui.add_lineInLog("Erro ao acessar o bd");
		} catch (GrFingerJavaException e) {
			e.printStackTrace();
			// write error to log
			System.out.println(e.getMessage());
		}
		return false;
	}

	public void onFingerDown(String id_sensor) {
		ui.add_lineInLog("O sensor \"" + id_sensor + "\" foi disparado");

	}

	public void onFingerUp(String id_sensor) {
		ui.add_lineInLog("Sensor \"" + id_sensor + "\": Dedo removido.");
	}
}
