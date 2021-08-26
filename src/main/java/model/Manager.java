package model;

public class Manager {
        private static Manager manager_instance;
        private Integer loggedInUserId = null;

        Manager(){ }

        public static Manager getInstance(){
            if (manager_instance == null) manager_instance = new Manager();
            return manager_instance;
        }

        public int getLoggedInUserId() {
            return this.loggedInUserId;
        }

        public void setLoggedInUserId(Integer loggedInUserId) {
            this.loggedInUserId = loggedInUserId;
        }


}
