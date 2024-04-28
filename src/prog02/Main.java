package prog02;

/**
 * A program to query and modify the phone directory stored in csc220.txt.
 *
 * @author vjm
 */
public class Main {

    /**
     * Processes user's commands on a phone directory.
     *
     * @param fn The file containing the phone directory.
     * @param ui The UserInterface object to use
     *           to talk to the user.
     * @param pd The PhoneDirectory object to use
     *           to process the phone directory.
     */
    public static void processCommands(String fn, UserInterface ui, PhoneDirectory pd) {
        pd.loadData(fn);
        boolean changed = false;
        boolean askForExitConfirmation = false;

        String[] commands = {"Add/Change Entry", "Look Up Entry", "Remove Entry", "Save Directory", "Exit"};
        String[] yesno = new String[]{"YES", "NO"};
        String name, number;

        do {
            if (!askForExitConfirmation) {
                int c = ui.getCommand(commands);

                switch (c) {
                    case 0: // Add/Change Entry

                        //if not empty name
                        //For saved contact, change entry
                        //For unsaved contact, add entry
                        //if empty number, prompt to enter a valid number

                        name = ui.getInfo("Enter name");
                        if (name == null) {
                            break;
                        }

                        if (name.length() == 0) {
                            ui.sendMessage("Blank names are not allowed.");
                            break;
                        }

                        number = ui.getInfo("Enter number");
                        if (number == null) {
                            break;
                        }

                        String oldNumber = pd.addOrChangeEntry(name, number);
                        if (oldNumber == null) {
                            ui.sendMessage(name + " was added to the directory\nNew number: " + number);
                        } else {
                            ui.sendMessage("Number for " + name + " was changed\nOld number: " + oldNumber + "\nNew number: " + number);
                        }

                        changed = true;
                        break;

                    case 1: // Look Up Entry

                        // sub-cases:
                        //case1: cancel
                        //case2: empty name not allowed
                        //case3: saved name
                        //case4: unsaved name

                        name = ui.getInfo("Enter the name ");

                        number = pd.lookupEntry(name);

                        // Empty name case
                        if (name != null && name.length() == 0) {

                            ui.sendMessage("Blank Names are not allowed");
                            // unsaved name
                        } else if (name != null ){

                            pd.lookupEntry(name);

                            //if the contact is unsaved
                            if (number == null) {

                                ui.sendMessage("This name you entered is an unsaved contact!");


                                //if the contact has indeed been saved
                            }else{

                                ui.sendMessage( name + " has number " + number );

                            }
                        }

                        break;



                    case 2: // Remove Entry
                        name = ui.getInfo("Enter name");

                        if(name != null && name.length() == 0){

                            ui.sendMessage("You can't remove a Blank Contact");

                        }else if (name != null && name.length() != 0){


                            number = pd.lookupEntry(name);
                            pd.removeEntry(name);

                            if( number == null){

                                ui.sendMessage("The name you entered has not been saved yet so it cannot be removed");
                            }else{

                                ui.sendMessage("You have removed " + name + " with number " +  number);
                                changed = true;
                            }


                        }

                        break;




                    case 3: // Save Entry
                        pd.save();

                        ui.sendMessage("Directory has been saved.");

                        changed = false;

                        break;




                    case 4: // Exit

                        if (!changed) {

                            return;
                        }

                        ui.sendMessage("The directory has changed. I am going to ask you if you want to exit without saving.");

                        askForExitConfirmation = true;


                        break;
                }
            } else {

                int choice = ui.getCommand(yesno);

                if (choice == 0) { //choose "YES" to exit without saving

                    return;

                } else if (choice == 1) { //choose "NO" to not exit and return to options

                    askForExitConfirmation = false;
                }
            }
        } while (true);
    }



    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String fn = "csc220.txt";
        // PhoneDirectory pd = new ArrayBasedPD();
        PhoneDirectory pd = new SortedPD();
        //UserInterface ui = new ConsoleUI();

        UserInterface ui = new GUI("Phone Directory");
        //UserInterface ui = new TestUI("Phone Directory");
        processCommands(fn, ui, pd);
    }
}
