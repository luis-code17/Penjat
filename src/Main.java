import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        ArrayList<Users> usersList = loadUsers();
        ArrayList<Words> parules = loadWords();


        addAdminAndWord(usersList, parules);

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
                        manageAdmin(sc, usersList, parules);
                    } else { // Si es usuari comu
                        manageUser(sc, parules);
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
    public static void manageAdmin(Scanner sc, ArrayList<Users> usersList, ArrayList<Words> parules) {
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
                    printWords(parules);
                    break;
                case 3:
                    addWords(parules, sc);
                    break;
                case 4:
                    newGame(parules, sc);
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
     * @param parules
     */
    public static void manageUser(Scanner sc, ArrayList<Words> parules) {
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
                    newGame(parules, sc);
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
    public static void newGame(ArrayList<Words> parules, Scanner sc) {
        int random = (int) (Math.random() * parules.size()); // Genera un numero aleatori entre 0 i la mida de l'arraylist
        String word = parules.get(random); // Obtenim la paraula aleatoria
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
    public static void addAdminAndWord(ArrayList<Users> usersList, ArrayList<Words> parules) {
        boolean admin = false;
        boolean poma = false;
        for (Users u : usersList) {
            if (u.getUser().equals("admin")) {
                admin = true;
            }
        }
        for (String p : parules) {
            if (p.equals("poma")) {
                poma = true;
            }
        }
        if (!admin) {
            usersList.add(new Users("Admin", "admin", "admin", true, 0));
        }
        if (!poma) {
            parules.add("poma");
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


    //Word

    /**
     * Comprova si la paraula existeix
     *
     * @param parules
     * @param word
     * @return
     */
    public static boolean checkWord(ArrayList<Words> parules, String word) {
        for (String p : parules) {
            if (p.equals(word)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Guarda les paraules a un fitxer
     *
     * @param parules
     */
    public static void saveWords(ArrayList<Words> parules) {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("words.txt"));
            for (String p : parules) {
                out.writeObject(p);
            }

            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Carrega les paraules del fitxer
     *
     * @return
     */
    public static ArrayList<Words> loadWords() {
        File file = new File("words.txt");
        if (!file.exists()) {
            return new ArrayList<>();
        }
        ArrayList<String> words = new ArrayList<>();
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            while (true) {
                try {
                    String word = (String) in.readObject();
                    words.add(word);
                } catch (EOFException e) {
                    break; // End of file reached
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return words;
    }

    /**
     * Mostra les paraules
     *
     * @param parules
     */
    public static void printWords(ArrayList<Words> parules) {
        for (String p : parules) {
            System.out.println(p);
        }
    }

    /**
     * Afegeix paraules y les guarda al fitxer
     *
     * @param parules
     * @param sc
     */
    public static void addWords(ArrayList<Words> parules, Scanner sc) {
        String word;
        sc.nextLine(); // Clear the buffer
        do {
            System.out.print("Digues la paraula que vols afegir: ");
            word = sc.nextLine();
            if (!word.matches("[A-Za-z]+")) {
                System.out.println("Nom invalid");
            }
        } while (!word.matches("[A-Za-z]+"));

        boolean exists = checkWord(parules, word);
        if (exists) {
            System.out.println("La paraula ja existeix");
        } else {
            parules.add(word);
            saveWords(parules);
            System.out.println("Paraula afegida correctament");
        }
    }

}

