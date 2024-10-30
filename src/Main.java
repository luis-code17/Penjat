import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Users> usersList = loadUsers();
        ArrayList<Words> paraules = loadWords();


        addAdminAndWord(usersList, paraules);

        Scanner sc = new Scanner(System.in);
        int option;
        boolean userMenu;
        do {
            option = optionLogin(sc);
            switch (option) {
                case 1:
                    registrer(sc, usersList);
                    break;
                case 2:
                    userMenu = login(sc, usersList);
                    if (userMenu) { // Si es admin
                        manageAdmin(sc, usersList, paraules);
                    } else { // Si es usuari comu
                        manageUser(sc, paraules);
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
        System.out.println("|       3. Afegir paraules    |");
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
     * @param usersList
     */
    public static void manageAdmin(Scanner sc, ArrayList<Users> usersList, ArrayList<Words> paraules) {
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
                    printUsers(usersList);
                    break;
                case 2:
                    printWords(paraules);
                    break;
                case 3:
                    addWords(paraules, sc);
                    break;
                case 4:
                    newGame(paraules, sc);
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
     * @param paraules
     */
    public static void manageUser(Scanner sc, ArrayList<Words> paraules) {
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
                    newGame(paraules, sc);
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
     * @param usersList
     */
    public static void registrer(Scanner sc, ArrayList<Users> usersList) {
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
                System.out.println("Nom invalid");
            }
        } while (!name.matches("[A-Za-z]+"));

        do {
            System.out.print("Digues el teu nom de pila: ");
            user = sc.nextLine();
        } while (user.length() < 3 || user.length() > 10);

        do {
            System.out.print("Digues la contrasenya: ");
            password = sc.nextLine();
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
     * @param usersList
     * @return
     */
    public static boolean login(Scanner sc, ArrayList<Users> usersList) {
        String user;
        String password;
        boolean login = false;
        boolean admin = false;

        sc.nextLine(); // Clear the buffer

        do {
            System.out.print("Digues el teu nom de pila: ");
            user = sc.nextLine();
            System.out.print("Digues la contrasenya: ");
            password = sc.nextLine();

            // Compara les dades introduides amb les de l'arraylist
            for (Users u : usersList) {
                if (u.getUser().equals(user) && u.getPassword().equals(password)) {
                    login = true;
                    System.out.println("Benvingut " + u.getName());

                    if (u.getAdmin() == true) {
                        admin = true; // Si es admin
                    }

                    break;
                }
            }

            if (!login) {
                System.out.println("Usuari o contrasenya incorrectes");
            }

        } while (!login);

        return admin;

    }

    /**
     * Joc del penjat amb les paraules de l'arraylist
     */
    public static void newGame(ArrayList<Words> paraules, Scanner sc) {
        int random = (int) (Math.random() * paraules.size()); // Genera un numero aleatori entre 0 i la mida de l'arraylist
        String word = paraules.get(random); // Obtenim la paraula aleatoria
        char[] wordArray = word.toCharArray(); // Convertim la paraula en un array de caracters
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
                end = true;
            }
            if (correct == wordArray.length) {
                System.out.println("Has guanyat!, la paraula era: " + word);
                end = true;
            }
        } while (!end);

    }

    /**
     * Funció per agregar l' usuari admin i la paraula poma al fitxer si no n'hi ha
     */
    public static void addAdminAndWord(ArrayList<Users> usersList, ArrayList<Words> paraules) {
        boolean admin = false;
        boolean poma = false;
        for (Users u : usersList) {
            if (u.getUser().equals("admin")) {
                admin = true;
            }
        }
        for (Words p : paraules) {
            if (p.getWord().equals("poma")) {
                poma = true;
            }
        }
        if (!admin) {
            usersList.add(new Users("Admin", "admin", "admin", true, 0));
        }
        if (!poma) {
            paraules.add(new Words("poma", 1));
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
     * @param usersList
     */
    public static void printUsers(ArrayList<Users> usersList) {
        for (Users u : usersList) {
            System.out.println(u.toString());
        }
    }


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
     * Desa les paraules al fitxer words.dat en format UTF-8 (50 bytes per paraula i 8 bytes per puntuació).
     * @param words L'arraylist de paraules a desar al fitxer words.dat
     */
    public static void saveWords(ArrayList<Words> words) {
        try (RandomAccessFile file = new RandomAccessFile("words.dat", "rw")) {
            for (Words word : words) {
                //si la paraula és més curta de 50 bytes, omplirà amb espais
                String formattedWord = String.format("%-50s", word.getWord());
                /*
                 * "%-50s" s'utilitza en Java per formatar cadenes de text:
                 *
                 * %: Indica l'inici d'una especificació de format.
                 * -: Indica que el text s'alinearà a l'esquerra. Sense el -, la cadena s'alinearia a la dreta.
                 * 50: Especifica l'ample mínim del camp; la cadena ocuparà almenys 50 caràcters.
                 *     Si la cadena és més curta, es completarà amb espais en blanc a la dreta.
                 */


                // Escriure la paraula com a bytes (50 bytes)
                file.write(formattedWord.getBytes("UTF-8"));
                //utilitzem UTF-8 per a que no hi hagi problemes amb els caràcters especials

                // Escriure la puntuació com a long (8 bytes)
                file.writeLong(word.getPoints()); // Escriu la puntuació com a long (8 bytes)
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
            String wordString = file.readUTF().trim(); // Llegeix la paraula i elimina espais en blanc
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
     * @param paraules
     */
    public static void printWords(ArrayList<Words> paraules) {
        for (Words p : paraules) {
            System.out.println(p);
        }
    }

    /**
     * Afegeix paraules y les guarda al fitxer
     *
     * @param paraules
     * @param sc
     */
    public static void addWords(ArrayList<Words> paraules, Scanner sc) {
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

}

