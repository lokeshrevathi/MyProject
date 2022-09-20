import java.lang.NumberFormatException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

class EmployeeController {
    int traineeId = 1;
    int trainerId = 1;
    TraineeService traineeService = new TraineeService();
    TrainerService trainerService = new TrainerService();
    Scanner scanner = new Scanner(System.in);

    public static void main(String args[]) {
        int choice;
        Scanner scanInput = new Scanner(System.in);
        EmployeeController controller = new EmployeeController();

        do {
            System.out.println("\n1->Enroll for trainer."
                               .concat("\n2->Enroll for trainee.")
                               .concat("\n3<-Exit."));
            choice = controller.getChoice();
            switch (choice) {
                case 1:
                    controller.manageTrainer();
                    break;

                case 2:
                    controller.manageTrainee();
                    break;

                case 3:
                    System.out.println("-----<EXIT>-----");
                    break;

                default :
                    System.out.println("Please select valid option!\n");
            }
        } while (3 != choice);
    }

    public void manageTrainee() {
        int choice;
        do {
            System.out.println("\n1->Create\n2->Read\n3->Search\n4->Update" 
                             .concat("\n5->Delete\n6<-Go Back"));
            choice = getChoice();
            switch (choice) {
                case 1:
                    createTrainee();
                    break;

                case 2:
                    readTrainee();
                    break;

                case 3:
                    searchTrainee();
                    break;

                case 4:
                    updateTrainee();
                    break;

                case 5:
                    deleteTrainee();
                    break;

                case 6:
                    break;

                default:
                    System.out.println("Please select valid option!\n");
            }
        } while (6 != choice);
    }

    public void createTrainee() {
        Trainee trainee = new Trainee(getId("trainee"), getName("trainee"),
                                      getDob("trainee"), getRole("trainee"),
                                      getPhoneNo("trainee"), getMailId("trainee"),
                                      "not assigned", 0);
        traineeService.addDetails(trainee);
        System.out.println("Trainee details added!\n");
    }

    public Integer getId(String fieldName) {
        
        if (fieldName.equals("trainee")) {
            Integer id1 = new Integer(traineeId++);
            System.out.println("\nYours " + fieldName + " ID                  : "
                               + id1);
            return id1;
        } else {
            Integer id2 = new Integer(trainerId++);
            System.out.println("\nYours " + fieldName + " ID                  : "
                               + id2);
            return id2;
        }
    }

    public String getName(String fieldName) {
        String name;
        boolean isValidName = false;

        do {
            System.out.print("Enter " + fieldName + " name                : ");
            name = scanner.nextLine();
            isValidName = !(UtilValidation.isValid(UtilValidation.regexName, name));
            if (isValidName) {
                System.out.println("Please enter valid name....!");
            }
        } while (isValidName);
        return name;
    }

    public String getDob(String fieldName) {
        String dob;
        boolean isValidDob = false;

        do {
            System.out.print("Enter " + fieldName + " D.O.B               : ");
            dob = scanner.nextLine();
            isValidDob = !(UtilValidation.checkDob(dob));
            if (isValidDob) {
                System.out.println("Please enter valid D.O.B....!");
            }
        } while (isValidDob);
        return dob;
    }

    public String getRole(String fieldName) {
        String role;
        boolean isValidRole = false;

        do {
            System.out.print("Enter "  + fieldName + " role                : ");
            role = scanner.nextLine();
            isValidRole = !(UtilValidation.isValid(UtilValidation.regexRole, role));
            if (isValidRole) {
                System.out.println("Please enter valid role....!");
            }
        } while (isValidRole);
        return role;
    }

    public String getPhoneNo(String fieldName) {
        String phoneNo;
        boolean isValidPhoneNo = false;
        do {
            System.out.print("Enter " + fieldName + " phone number        : ");
            phoneNo = scanner.nextLine();
            isValidPhoneNo = !(UtilValidation.isValid(UtilValidation.regexPhoneNo, phoneNo));
            if (isValidPhoneNo) {
                System.out.println("Please enter valid phone number....!");
            } else {
                if (fieldName.equals("trainee")) {
                    isValidPhoneNo = !(traineeService.isDuplicate("phoneNo", phoneNo));
                    
                } else {
                    isValidPhoneNo = !(trainerService.isDuplicate("phoneNo", phoneNo));
                }
            }
        } while (isValidPhoneNo);
        return phoneNo;
    }

    public String getMailId(String fieldName) {
        String mailId;
        boolean isValidMailId = false;

        do {
            System.out.print("Enter " + fieldName + " mail Id             : ");
            mailId = scanner.nextLine();
            isValidMailId = !(UtilValidation.isValid(UtilValidation.regexMailId, mailId));  
            if (isValidMailId) {
                System.out.println("Please enter valid mail Id....!");
            } else {
                if (fieldName.equals("trainee")) {
                    isValidMailId = !(traineeService.isDuplicate("mailId", mailId));
                } else {
                    isValidMailId = !(trainerService.isDuplicate("mailId", mailId));
                }
            }
        } while (isValidMailId);
        return mailId;
    }

    public void readTrainee() {
        if (traineeService.checkListIsEmpty()) {
            System.out.println("no details can exist....!\n");
        } else {
            for (Trainee trainee : traineeService.getAllDetails()) {
                System.out.println("\n" + trainee.toString()); 
                Integer trainerID = trainee.getTrainerId();
                if (!(trainerID.equals(0))) {
                    System.out.println("Trainee assigned under : "
                                       + trainerID);
                }
            }
        }
    }

    public void searchTrainee() {
        System.out.print("Enter Trainee ID to search Trainee details: ");
        Integer id = getIdToManipulate();

        try {
            if (traineeService.ifTraineePresent(id)) {
                System.out.println(traineeService.displayTrainee(id));
                Integer trainerId1 = traineeService.getTrainerId(id);
                if (!(trainerId1.equals(0))) {
                    System.out.println("Trainee assigned under : "
                                       + traineeService.getTrainerId(id));
                }
            }
        } catch (AlreadyExistException existException) {
            System.out.println(existException);
        }
    }

    public void updateTrainee() {
        int choice;
        System.out.print("Enter trainee Id to update : ");
        Integer id = getIdToManipulate();

        try {
            if (traineeService.ifTraineePresent(id)) {
                do {
                    System.out.println("\n1-->Trainee Name.\n2-->Trainee D.O.B."
                                       .concat("\n3-->Trainee Role.")
                                       .concat("\n4-->Trainee Phone number.")
                                       .concat("\n5-->Trainee Mail Id.")
                                       .concat("\n6-->Go back."));
                    choice = getChoice();
                        switch (choice) {
                            case 1:
                                traineeService.updateTrainee(id, "name", getName("trainee"));
                                System.out.println("Trainee name is updated!");
                                break;

                            case 2:
                                traineeService.updateTrainee(id, "dob", getDob("trainee"));
                                System.out.println("Trainee D.O.B is updated!");
                                break;

                            case 3:
                                traineeService.updateTrainee(id, "role", getRole("trainee"));
                                System.out.println("Trainee Role is updated!");
                                break;

                            case 4:
                                traineeService.updateTrainee(id, "phoneNo", getPhoneNo("trainee"));
                                System.out.println("Trainee Address is updated!");
                                break;

                            case 5:
                                traineeService.updateTrainee(id, "mailId", getMailId("trainee"));
                                System.out.println("Trainee MailID is updated!");
                                break;

                            case 6:
                                break;

                            default:
                                System.out.println("Invalid choice");
                        }
                        Integer trainerID = traineeService.getTrainerId(id);
                        if (!(trainerID.equals(0))) {
                            trainerService.updateTrainee(trainerID, id,
                                                         traineeService.getTrainee(id));
                        }
                } while (choice != 6);
            }
        } catch (AlreadyExistException existException) {
            System.out.println(existException);
        }
    }

    public void deleteTrainee() {
        Integer id;
        Integer trainerID;
        List<Trainee> traineeList;
        System.out.print("Enter trainee ID to delete employee details: ");
        id = getIdToManipulate();

        try {
            if (traineeService.ifTraineePresent(id)) {
                trainerID = traineeService.getTrainerId(id);
                if (!(trainerID.equals(0))) {
                    trainerService.deleteTrainee(trainerID, id);
                }
                traineeService.deleteDetails(id);
                System.out.println("Trainee details deleted!\n");
            }
        } catch (AlreadyExistException existException) {
            System.out.println(existException);
        }
    }

    public List<Trainee> assignTrainee(Integer trainerId1) {
        int choice;
        Integer id;
        List<Trainee> traineeList = new ArrayList<Trainee>();
        do {
            System.out.println("\n1--->Assign already enrolled trainee."
                               .concat("\n2--->Enroll trainee and assign.")
                               .concat("\n3<---Go back."));
            choice = getChoice();
            switch (choice) {
                case 1:
                    List<Trainee> list = assignEnrolledtrainee(trainerId1);
                    if (!(list.isEmpty())) {
                        traineeList.addAll(list);
                    }
                    break;

                case 2:
                    list = createAssignTrainee(trainerId1);
                    if (!(list.isEmpty())) {
                        traineeList.addAll(list);
                    }
                    break;

                case 3:
                    break;

                default:
                    System.out.println("Invalid choice...!");
            }       
        } while (3 != choice);
        return traineeList;
    }

    public List<Trainee> assignEnrolledtrainee(Integer trainerId1) {
        List<Trainee> traineeList = new ArrayList<Trainee>();
        Integer id;
        if (traineeService.checkListIsEmpty()) {
            System.out.println("trainee details does not"
                               .concat(" exist...!\nPlease enroll")
                               .concat(" trainee details...!"));
        } else {
            System.out.println();
            List<Trainee> listOfTrainee = traineeService.getAllDetails();
            for (Trainee trainee : listOfTrainee) {
                System.out.println(trainee.getId() + "-->"
                                   + trainee.getName() + "--"
                                   + trainee.getStatus());
            }
            System.out.print("\nSelect trainee ID to"
                             .concat(" add trainee: "));
            id = getIdToManipulate();
            try {
                if (traineeService.ifTraineePresent(id)) {
                    if ((traineeService.getStatus(id))
                        .equals("not assigned")) {
                        traineeService.updateTrainee(id, "status", "assigned");
                        traineeService.updateTrainee(id, "trainerId", Integer
                                                     .toString(trainerId1));
                        traineeList.add(traineeService.getTrainee(id));
                    } else {
                        System.out.println("This trainee is already"
                                           .concat("assigned"));
                    }
                }
            } catch (AlreadyExistException existException) {
                System.out.println(existException);
            }
        }
        return traineeList;
    }

    public List<Trainee> createAssignTrainee(Integer trainerId1) {
        List<Trainee> traineeList = new ArrayList<Trainee>();
        createTrainee();
        Integer setId = new Integer(traineeId - 1);
        traineeService.updateTrainee(setId, "status", "assigned");
        traineeService.updateTrainee(setId, "trainerId", Integer.toString(trainerId1));
        traineeList.add(traineeService.getTrainee(setId));
        return traineeList;
    }

    /*public void modifyTeam() {
        displayTeamDetails();
    }

    public void displayTeamDetails() {

        if (!(trainerService.checkListIsEmpty())) {
            for (Trainer trainer : trainerService.getAllDetails()) {
                System.out.println("****************************");
                System.out.println(trainer.getId() + "-->" + trainer.getName());
                if (trainer.getTrainee().isEmpty()) {
                    System.out.println("No trainee's can assigned for -->" 
                                       + trainer.getName());
                } else {
                    System.out.println("trainee's assigned for -->" 
                                       + trainer.getName());
                    for (Trainee trainee : trainer.getTrainee()) {
                        System.out.println(trainee.getId() + "-->"
                                           + trainee.getName());
                    }
                }
                System.out.println("****************************");
            }
        } else {
            System.out.println("no trainer details can exist....!");
        }
    }*/

    public void manageTrainer() {
        int choice;
        do {
            System.out.println("\n1-->Create\n2-->Read\n3-->Search\n4-->Update" 
                             .concat("\n5-->Delete\n6<--Go Back"));
                             //.concat("\n7<--Go Back"));
            choice = getChoice();
            switch (choice) {
                case 1:
                    createTrainer();
                    break;

                case 2:
                    readTrainer();
                    break;

                case 3:
                    searchTrainer();
                    break;

                case 4:
                    updateTrainer();
                    break;

                case 5:
                    deleteTrainer();
                    break;

                /*case 6:
                    modifyTeam();
                    break;*/

                case 6:
                    break;

                default:
                    System.out.println("Please select valid option!\n");
            }
        } while (6 != choice);
    }

    public void createTrainer() {
        Integer id = getId("trainer");
        Trainer trainer = new Trainer(id, getName("trainer"),
                                      getDob("trainer"), getRole("trainer"),
                                      getPhoneNo("trainer"), getMailId("trainer"),
                                      getExperience());
        trainerService.addDetails(trainer);
        trainer.setTrainee(assignTrainee(id));
        trainerService.assignTrainee(id, trainer);
        System.out.println("Trainer details added!\n");
    }

    public void readTrainer() {
        if (trainerService.checkListIsEmpty()) {
            System.out.println("no details can exist....!\n");
        } else {
            for (Trainer trainerList : trainerService.getAllDetails()) {
                System.out.println("**********************************"
                                  .concat("****************"));
                System.out.println(trainerList.toString());
                if (!(trainerList.getTrainee().isEmpty())) {
                    System.out.println("\nTrainee's assigned for -> "
                                       + trainerList.getName());
                    for (Trainee trainerSubList : trainerList.getTrainee()) {
                        System.out.println(trainerSubList.toString());
                    }
                }
                System.out.println("**********************************"
                                  .concat("****************"));
            }
        }
    }

    public void searchTrainer() {
        System.out.print("Enter Trainer ID to search Trainer details: ");
        Integer id = getIdToManipulate();

        try {
            if (trainerService.ifTrainerPresent(id)) {
                System.out.println(trainerService.displayTrainer(id));
                if (!(trainerService.checkTraineeListIsEmpty(id))) {
                    System.out.println("\nTrainee's assigned for -> "
                                       + trainerService.getName(id));
                    for (Trainee trainee : trainerService.getTraineeList(id)) {
                        System.out.println(trainee.toString());
                    }
                }
            }
        } catch (AlreadyExistException existException) {
            System.out.println(existException);
        }
    }

    public void updateTrainer() {
        int choice;
        System.out.print("Enter trainer Id to update : ");
        Integer id = getIdToManipulate();
        try {
            if (trainerService.ifTrainerPresent(id)) {
                do {
                    System.out.println("\n1-->Trainer Name.\n2-->Trainer D.O.B."
                                       + "\n3-->Trainer Role."
                                       + "\n4-->Trainer Phone number."
                                       + "\n5-->Trainer Mail Id."
                                       + "\n6.Trainer Experience."
                                       + "\n7<--Go back." 
                                       + "\nEnter your choice: ");
                    choice = getChoice();
                        switch (choice) {
                            case 1:
                                trainerService.updateTrainer(id, "name",
                                                             getName("trainer"));
                                System.out.println("Trainer name is updated!");
                                break;

                            case 2:
                                trainerService.updateTrainer(id, "dob",
                                                             getDob("trainer"));
                                System.out.println("Trainer D.O.B is updated!");
                                break;

                            case 3:
                                trainerService.updateTrainer(id, "role",
                                                             getRole("trainer"));
                                System.out.println("Trainer Role is updated!");
                                break;

                            case 4:
                                trainerService.updateTrainer(id, "phoneNo",
                                                             getPhoneNo("trainer"));
                                System.out.println("Trainer Phone number "
                                                   .concat("is updated!"));
                                break;

                            case 5:
                                trainerService.updateTrainer(id, "mailId",
                                                             getMailId("trainer"));
                                System.out.println("Trainer MailID is updated!");
                                break;

                            case 6:
                                trainerService.updateTrainer(id, "experience",
                                                             Integer.toString(getExperience()));
                                System.out.println("Trainer experience "
                                                   .concat("is updated!"));
                                break;

                            default:
                                System.out.println("Invalid choice");
                        }
                } while (7 != choice);
            }
        } catch (AlreadyExistException existException) {
            System.out.println(existException);
        }
    }

    public void deleteTrainer() {
        System.out.print("Enter trainer ID to delete employee details: ");
        Integer id = getIdToManipulate();
        try {
            if (trainerService.ifTrainerPresent(id)) {
                for (Trainee trainee : trainerService.getTraineeList(id)) {
                    traineeService.updateTrainee(trainee.getId(), "status",
                                                 "not assigned");
                    traineeService.updateTrainee(trainee.getId(), "trainerId", Integer
                                                 .toString(0));
                }
                trainerService.deleteTrainer(id);
                System.out.println("Trainer details deleted!\n");
            }
        } catch (AlreadyExistException existException) {
            System.out.println(existException);
        }
    }

    public int getExperience() {
        int flag = 0;
        int experience = 0;
        do {
            try {
                System.out.print("Enter Years of experience         : ");
                experience = Integer.parseInt(scanner.nextLine());
                flag = 0;
            } catch (NumberFormatException exception) {
                flag = 1;
                System.out.println("invalid value....!");
            }
        } while (1 == flag);
        return experience;
    }

    public int getChoice() {
        int flag = 0;
        int choice = 0;
        do {
            try {
                System.out.print("Enter your choice         : ");
                choice = Integer.parseInt(scanner.nextLine());
                flag = 0;
            } catch (NumberFormatException exception) {
                flag = 1;
                System.out.println("invalid option....!");
            }
        } while (1 == flag);
        return choice;
    }

    public int getIdToManipulate() {
        int flag = 0;
        int choice = 0;
        do {
            try {
                choice = Integer.parseInt(scanner.nextLine());
                flag = 0;
            } catch (NumberFormatException exception) {
                flag = 1;
                System.out.println("invalid option....!");
            }
        } while (1 == flag);
        return choice;
    }
}