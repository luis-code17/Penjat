import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        checkFile();
        addAdminAndWord();

        Scanner sc = new Scanner(System.in);
        int option;
        Users user;
        do {
            option = optionLogin(sc);
            switch (option) {
                case 1:
                    registrer(sc);
                    break;
                case 2:
                    user = login(sc);
                    if (user.getAdmin()) { // Si es admin
                        manageAdmin(sc, user);
                    } else { // Si es usuari comu
                        manageUser(sc, user);
                    }
                    break;
                case 3:
                    System.out.println("Adeu!");
                    break;
            }
        } while (option != 3);
    }


    //Menus

    /**
     * Menu de login
     */
    public static void menuLogin() {
        System.out.println("|-----------------------------|");
        System.out.println("| Benvingut al joc del penjat |");
        System.out.println("|        Què vols fer?        |");
        System.out.println("|-----------------------------|");
        System.out.println("|       1. Registrar-se       |");
        System.out.println("|       2. Login              |");
        System.out.println("|       3. Sortir             |");
        System.out.println("|-----------------------------|");
        System.out.print("Tria una opció: ");
    }

    /**
     * Menu de l'administrador
     */
    public static void menuAdmin() {
        System.out.println("|-----------------------------|");
        System.out.println("|  Menu Admin, què vols fer?  |");
        System.out.println("|-----------------------------|");
        System.out.println("|       1. Llistar usuaris    |");
        System.out.println("|       2. Llistar paraules   |");
        System.out.println("|       3. Editar paraules    |");
        System.out.println("|       4. Jugar              |");
        System.out.println("|       5. Sortir             |");
        System.out.println("|-----------------------------|");
        System.out.print("Tria una opció: ");
    }

    /**
     * Menu de l'usuari
     */
    public static void menuUser() {
        System.out.println("|-----------------------------|");
        System.out.println("|     Hola, què vols fer?     |");
        System.out.println("|-----------------------------|");
        System.out.println("|          1. Jugar           |");
        System.out.println("|          2. Sortir          |");
        System.out.println("|-----------------------------|");
        System.out.print("Tria una opció: ");
    }


    //Mange

    /**
     * Gestiona la navegació de l'administrador
     *
     * @param sc
     */
    public static void manageAdmin(Scanner sc, Users user) {
        int option;
        do {
            do {
                menuAdmin();
                if (!sc.hasNextInt()) {
                    sc.next();
                    option = 0;
                } else {
                    option = sc.nextInt();
                }
                if (option < 1 || option > 5) {
                    System.out.println("Opció invalida. Torna a triar");
                }
            } while (option < 1 || option > 5);

            switch (option) {
                case 1:
                    printUsers();
                    break;
                case 2:
                    printWords();
                    break;
                case 3:
                    editWord(sc);
                    break;
                case 4:
                    newGame(sc, user);
                    break;
                case 5:
                    System.out.println("Adeu, admin!");
                    break;
            }
        } while (option != 5);
    }

    /**
     * Gestiona la navegació de l'usuari
     *
     * @param sc
     */
    public static void manageUser(Scanner sc, Users user) {
        int option;
        do {
            do {
                menuUser();
                if (!sc.hasNextInt()) {
                    sc.next();
                    option = 0;
                } else {
                    option = sc.nextInt();
                }
                if (option < 1 || option > 2) {
                    System.out.println("Opció invalida. Torna a triar");
                }
            } while (option < 1 || option > 2);

            switch (option) {
                case 1:
                    newGame(sc, user);
                    break;
                case 2:
                    System.out.println("Adeu!");
                    break;
            }
        } while (option != 2);
    }


    //Funcions auxiliars

    /**
     * Retorna l'opció de l'usuari
     *
     * @param sc
     * @return
     */
    public static int optionLogin(Scanner sc) {
        int option;
        do {
            menuLogin();
            if (!sc.hasNextInt()) {
                sc.next();
                option = 0;
            } else {
                option = sc.nextInt();
            }
            if (option < 1 || option > 3) {
                System.out.println("Opció invalida. Torna a triar");
            }
        } while (option < 1 || option > 3);
        return option;
    }

    /**
     * Registra un usuari
     *
     * @param sc
     */
    public static void registrer(Scanner sc) {
        ArrayList<Users> usersList = loadUsers();
        String name;
        String user;
        String password;
        int punts = 0;
        boolean admin = false;

        // Limpiem el buffer abans de llegir les dades
        sc.nextLine();

        // Solo acepta caracteres de la A-Z y a-z
        do {
            System.out.print("Digues el teu nom: ");
            name = sc.nextLine();
            if (!name.matches("[A-Za-z]+")) {
                System.out.println("Nom invalid, només pots introduir lletres");
            }
        } while (!name.matches("[A-Za-z]+"));

        do {
            System.out.print("Digues el teu nom de pila: ");
            user = sc.nextLine();
            if (user.length() < 3 || user.length() > 10) {
                System.out.println("Nom invalid, ha de tenir entre 3 i 10 caracters");
            }
        } while (user.length() < 3 || user.length() > 10);

        do {
            System.out.print("Digues la contrasenya: ");
            password = sc.nextLine();
            if (password.length() < 3 || password.length() > 10) {
                System.out.println("Contrasenya invalida, ha de tenir entre 3 i 10 caracters");
            }
        } while (password.length() < 3 || password.length() > 10);

        boolean exists = checkUser(usersList, user, password);
        if (exists) {
            System.out.println("L'usuari ja existeix");
        } else {
            Users userN = new Users(name, user, password, admin, punts);
            usersList.add(userN);

            saveUser(usersList);
            System.out.println("Usuari creat correctament");
        }
    }

    /**
     * Login de l'usuari
     *
     * @param sc
     * @return
     */
    public static Users login(Scanner sc) {

        ArrayList<Users> usersList = loadUsers();
        String user;
        String password;
        boolean login = false;
        Users userReturn = null;

        sc.nextLine(); // Clear the buffer

        do {
            System.out.print("Digues el teu nom de pila: ");
            user = sc.nextLine();
            System.out.print("Digues la contrasenya: ");
            password = sc.nextLine();

            // Compara les dades introduides amb les de l'arraylist
            for (Users u : usersList) {
                if (u.getUser().equals(user) && u.getPassword().equals(password)) {
                    userReturn = u;
                    login = true;
                    System.out.println("Benvingut " + u.getName());
                    break;
                }
            }

            if (!login) {
                System.out.println("Usuari o contrasenya incorrectes");
            }

        } while (!login);

        return userReturn;

    }

    /**
     * Joc del penjat amb les paraules de l'arraylist
     */
    public static void newGame(Scanner sc, Users user) {
        ArrayList<Words> paraules = loadWords();
        int random = (int) (Math.random() * paraules.size()); // Genera un numero aleatori entre 0 i la mida de l'arraylist
        Words word = loadWord(random); // Obtenim la paraula aleatoria

        // Obtenim la paraula aleatoria
        char[] wordArray = word.getWord().toCharArray(); // Convertim la paraula en un array de caracters
        int points = word.getPoints(); // Obtenim els punts de la paraula
        char[] wordHidden = new char[wordArray.length]; // Array de caracters ocults
        for (int i = 0; i < wordArray.length; i++) { // Omplim l'array de caracters ocults amb guions baixos
            wordHidden[i] = '_';
        }

        int errors = 0;
        int correct = 0;
        char letter; // Lletra introduida per l'usuari
        boolean end = false; // Final del joc
        do {
            //nomes acepta una lletra
       do {
            System.out.println(wordHidden);
            System.out.print("Introdueix una lletra: ");

            letter = sc.next().charAt(0);
            // Només accepta caràcters de la A-Z i a-z
            if (!Character.isLetter(letter)) {
                System.out.println("Només pots introduir lletres.");
                System.out.print("Introdueix una lletra: ");
                sc.next(); // Clear the invalid input
            }
        } while (!Character.isLetter(letter)); // Només accepta caràcters de la A-Z i a-z

            //conerteix la lletra en minuscula per a que no hi hagi problemes amb les majuscules
            letter = Character.toLowerCase(letter);

            boolean found = false; // Lletra trobada a la paraula

            // Comprovem si la lletra introduida es troba a la paraula
            for (int i = 0; i < wordArray.length; i++) {
                if (wordArray[i] == letter) {
                    wordHidden[i] = letter;
                    correct++;
                    found = true;
                }
            }
            if (!found) { // Si la lletra no es troba a la paraula incrementem els errors
                errors++;
                System.out.println("Lletra incorrecta. Errors: " + errors + "/10");

            }
            if (errors == 10) { // Si es compleixen 10 errors el joc finalitza
                System.out.println("Has perdut!");
                user.setPunts(user.getPunts() - 5);
                // Si l'usuari es queda amb punts negatius, els punts es posen a 0
                if (user.getPunts() < 0) {
                    user.setPunts(0);
                }
                updateUser(user);
                end = true;
            }
            if (correct == wordArray.length) {
                System.out.println("Has guanyat!, la paraula era: " + word);
                user.setPunts(user.getPunts() + points);
                updateUser(user);
                end = true;
            }
        } while (!end);

    }

    /**
     * Funció per agregar l' usuari admin i la paraula poma al fitxer si no n'hi ha
     */
    public static void addAdminAndWord() {
        // Carregar la llista d'usuaris
        ArrayList<Users> usersList = loadUsers();
        // Carregar la llista de paraules
        ArrayList<Words> wordsList = loadWords();
        // Variables per comprovar si existeixen l'admin i la paraula "poma"
        boolean adminExists = false;
        boolean appleExists = false;

        // Comprovar si existeix l'usuari "admin"
        for (Users user : usersList) {
            if (user.getUser().equals("admin")) {
                adminExists = true;
                break; // Sortir del bucle si ja s'ha trobat l'administrador
            }
        }

        // Comprovar si existeix la paraula "poma"
        for (Words word : wordsList) {
            if (word.getWord().equals("poma")) {
                appleExists = true;
                break; // Sortir del bucle si ja s'ha trobat la paraula
            }
        }

        // Afegir l'administrador si no existeix
        if (!adminExists) {
            usersList.add(new Users("Admin", "admin", "admin", true, 0));
        }

        // Afegir la paraula "poma" si no existeix
        if (!appleExists) {
            wordsList.add(new Words("poma", 1));
        }

        // Guardar les llistes actualitzades
        saveUser(usersList);
        saveWords(wordsList);
    }


    /**
     * Comprova si els fitxers users.dat i words.dat existeixen, si no, els crea
     */
    public static void checkFile() {
        File file = new File("users.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        file = new File("words.dat");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //User

    /**
     * Guarda els usuaris a un fitxer
     *
     * @param usersList
     */
    public static void saveUser(ArrayList<Users> usersList) {

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("users.dat"));
            for (Users u : usersList) {
                out.writeObject(u);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega els usuaris del fitxer
     *
     * @return
     */
    public static ArrayList<Users> loadUsers() {
        File file = new File("users.dat");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        ArrayList<Users> users = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    Users user = (Users) in.readObject();
                    users.add(user);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return users;
    }

    /**
     * Comprova si l'usuari existeix
     *
     * @param usersList
     * @param user
     * @param password
     * @return
     */
    public static boolean checkUser(ArrayList<Users> usersList, String user, String password) {
        for (Users u : usersList) {
            if (u.getUser().equals(user) && u.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Mostra els usuaris
     *
     */
    public static void printUsers() {
        ArrayList<Users> usersList = loadUsers();
        for (Users u : usersList) {
            System.out.println(u.toString());
        }
    }

    /**
     * Actualitza un usuari al fitxer users.dat amb les dades del paràmetre user
     * @param user
     */
    public static void updateUser(Users user) {
        ArrayList<Users> usersList = loadUsers(); // Carrega la llista d'usuaris
        for (Users u : usersList) {
            if (u.getUser().equals(user.getUser())) {
                //borro u
                usersList.remove(u);
                //afegir user
                usersList.add(user);
                break; // Sortir del bucle després d'actualitzar
            }
        }
        saveUser(usersList); // Desa la llista d'usuaris actualitzada
    }



    //Words
    /**
     * Comprova si la paraula existeix
     *
     * @param paraules  arraylist de paraules (Words)
     * @param wordString paraula a comprovar
     * @return true si existeix, false si no
     */
    public static boolean checkWord(ArrayList<Words> paraules, String wordString) {
        for (Words p : paraules) {
            if (p.getWord().equals(wordString) ) { //comprova si la paraula coincideix amb els de l'arraylist
                return true;
            }
        }
        return false;
    }

    /**
     * Desa les paraules al fitxer words.dat (50 bytes per paraula i 8 bytes per puntuació).
     * @param words L'arraylist de paraules a desar al fitxer words.dat
     */
    public static void saveWords(ArrayList<Words> words) {
        try (RandomAccessFile file = new RandomAccessFile("words.dat", "rw")) {
            file.setLength(0); // Esborra el contingut del fitxer abans de guardar

            for (Words word : words) {
                // Si la paraula és més curta de 50 bytes, omplirà amb espais
                String formattedWord = String.format("%-50s", word.getWord());
                /*
                 * "%-50s" s'utilitza en Java per formatar cadenes de text:
                 * %: Indica l'inici d'una especificació de format.
                 * -: Indica que el text s'alinearà a l'esquerra. Sense el -, la cadena s'alinearia a la dreta.
                 * 50: Especifica l'ample mínim del camp; la cadena ocuparà almenys 50 caràcters.
                 *     Si la cadena és més curta, es completarà amb espais en blanc a la dreta.
                 */

                // Escriure la paraula com a bytes (50 bytes)
                file.write(formattedWord.getBytes());

                // Escriure la puntuació com a int (4 bytes)
                file.writeInt(word.getPoints()); // Escriu la puntuació com a int (4 bytes)
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * Carrega una paraula del fitxer words.dat
     * @param num El número de la paraula a carregar
     * @return La paraula carregada
     */
    public static Words loadWord(int num) {
        Words word = null;
        // calcula quantes paraules hi ha file size 54b?
        try (RandomAccessFile file = new RandomAccessFile("words.dat", "r")) { // Obre el fitxer en mode lectura
            // El fitxer es llegeix en mode RandomAccessFile perquè les paraules ocupen 50 bytes i la puntuació 4 bytes (int).
            int bytesFile = (int) file.length();
            int numWords = bytesFile / 54;

            file.seek(num*54);
            // Llegeix la paraula del fitxer (se suposa que ocupa 50 bytes)
            byte[] wordBytes = new byte[50];
            file.readFully(wordBytes);
            String wordString = new String(wordBytes).trim(); // Llegeix la paraula i elimina espais en blanc

            int points = file.readInt(); // Llegeix la puntuació del fitxer
            word = new Words(wordString, points);

        } catch (IOException e) { // Maneig d'excepcions en cas d'errors d'entrada/sortida
            e.printStackTrace(); // Imprimeix l'error si ocorre una excepció
        }


        return word;
    }


    /**
     * Carrega les paraules del fitxer words.dat en un arraylist utilitzant la funció loadWord
     * @return L'arraylist de paraules carregades
     */
    public static ArrayList<Words> loadWords() {
        ArrayList<Words> words = new ArrayList<>();
        //for con la funcion loadWord
        try (RandomAccessFile file = new RandomAccessFile("words.dat", "r")) {
            int bytesFile = (int) file.length();
            int numWords = bytesFile / 54;
            for (int i = 0; i < numWords; i++) {
                Words word = loadWord(i);
                words.add(word);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words; // Retorna la llista de paraules carregades
    }

    /**
     * Mostra les paraules
     *
     */
    public static void printWords() {
        ArrayList<Words> paraules = loadWords();
        for (Words p : paraules) {
            System.out.println(p);
        }
    }

    /**
     * Afegeix paraules y les guarda al fitxer
     *
     * @param sc
     */
    public static void addWords(Scanner sc) {
        ArrayList<Words> paraules = loadWords();
        String wordString;
        sc.nextLine(); // Clear the buffer
        int points = 0;
        do {
            System.out.print("Digues la paraula que vols afegir: ");
            wordString = sc.nextLine();
            if (!wordString.matches("[A-Za-z]+")) {
                System.out.println("Nom invalid");
            }
        } while (!wordString.matches("[A-Za-z]+"));

        do {
            System.out.print("Digues els punts de la paraula: ");
            if (!sc.hasNextInt()) {
                sc.next();
                System.out.println("Introdueix un número, si us plau");
                points = 0;
            } else {
                points = sc.nextInt();
            }
            if (points <= 0) {
                System.out.println("Punts invalids");
                System.out.println("Els punts han de ser més grans que 0");
            }
        }while (points <= 0);

        boolean exists = checkWord(paraules, wordString);
        if (exists) {
            System.out.println("La paraula ja existeix");
        } else {
            paraules.add(new Words(wordString, points));
            saveWords(paraules);
            System.out.println("Paraula afegida correctament");
        }
    }

    /**
     * Actualitza una paraula al fitxer words.dat amb les dades del paràmetre word
     * @param word
     */
    public static void updateWord(Words word) {
        ArrayList<Words> paraules = loadWords(); // Carrega la llista de paraules
        // Obté la paraula actual de la llista
        for (Words p : paraules) {
            if (p.getWord().equals(word.getWord())) {
                //borro p
                paraules.remove(p);
                //afegir word
                paraules.add(word);
                break; //  Sortir del bucle després d'actualitzar
            }
        }
        saveWords(paraules); // Desa les paraules actualitzades
    }


    /**
     * Edita la puntuació de una paraula i la guarda al fitxer words.dat (50 bytes per paraula i 4 bytes per puntuació).
     * @param sc
     */
    public static void editWord(Scanner sc) {
        ArrayList<Words> paraules = loadWords(); // Carrega la llista de paraules
        int index = 0;

        // Mostra les paraules amb el seu índex
        for (Words p : paraules) {
            System.out.println(index + ", " + p);
            index++;
        }

        sc.nextLine(); // Neteja el buffer
        int pos = -1; // Inicialitza la posició
        char addW; // Caràcter per a l'opció d'afegir
        boolean option = false; // Variable per controlar si s'ha seleccionat una opció

        // Bucle per seleccionar la paraula a editar
        do {
            System.out.println("Introdueix N per afegir una nova paraula");
            System.out.print("Digues el numero de la paraula que vols editar: ");

            if (!sc.hasNextInt()) {
                // Llegeix el pròxim valor com a String i obté el primer caràcter
                addW = sc.next().charAt(0); // Llegeix el primer caràcter de l'entrada

                // Comprova si és 'n' o 'N'
                if (Character.toLowerCase(addW) == 'n') {
                    addWords(sc); // Crida a la funció per afegir una nova paraula
                    option = true; // Marca que s'ha seleccionat una opció
                    return; // Sortir de la funció si s'afegeix una nova paraula
                } else {
                    System.out.println("Introdueix un numero entre 0 i " + (paraules.size() - 1) + " o N per afegir una nova paraula:");
                }
            } else {
                pos = sc.nextInt(); // Només llegir el número si ha estat un int
                if (pos < 0 || pos >= paraules.size()) {
                    System.out.println("Número invalid");
                    System.out.println("Introdueix un numero entre 0 i " + (paraules.size() - 1) + ":");
                } else {
                    option = true; // S'assigna només si l'entrada és vàlida
                }
            }
        } while (!option); // Repetir fins que es seleccioni una opció vàlida

        Words word = loadWord(pos);
        System.out.println("Paraula seleccionada: " + word);

        // Edició de punts
        int points = 0;
        do {
            System.out.print("Digues els punts de la paraula: ");
            if (!sc.hasNextInt()) {
                sc.next(); // Netejar el buffer
                System.out.println("Introdueix un número, si us plau");
                points = 0; // Reiniciar la puntuació
            } else {
                points = sc.nextInt();
                if (points > 0) {
                    word.setPoints(points); // Actualitzar els punts de la paraula
                    System.out.println("Paraula editada: " + word);
                    updateWord(word); // Actualitzar la paraula
                } else {
                    System.out.println("Punts invalids. Els punts han de ser més grans que 0.");
                }
            }
        } while (points <= 0); // Repetir fins que s'introdueixi un número vàlid
        System.out.println("Paraula editada correctament"); // Missatge d'èxit
    }





}


