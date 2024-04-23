public class Task {

    private String name;
    private String description;
    private Status status;
    private int id;

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
            return id;
        }

        public String getName() {
                    return name;
            }

        public String getDescription() {
                return description;
        }

        public Status getStatus() {
                return status;
        }

        public Task(String name, String description) {
                this.name = name;
                this.description = description;

        }
}