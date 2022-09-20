class AlreadyExistException extends Exception {
    String exceptionMessage;
 
    public AlreadyExistException(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String toString() {
        return exceptionMessage;
    }
}