import java.lang.String;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public enum DukeCommand {
    /**
     * Enum Class for all Duke Commands.
     */
    LIST {
        @Override
        public void execute(String s1) {
            Duke.HorizontalLine();
            // Print all the tasks added

            int counter = 1;
            for (Task currtask : Duke.listOfTask) {
                System.out.println(counter++ + "." + currtask);
            }

            Duke.HorizontalLine();
        }
    },
    DONE {
        @Override
        public void execute(String s1) {
            // Split the string to get the
            // index of the task to be done
            String[] arr = s1.split("\\s+");
            int pos = Integer.parseInt(arr[1]) - 1;

            Duke.HorizontalLine();
            System.out.println("Nice! I've marked this as done:");
            (Duke.listOfTask).get(pos).taskIsDone(); // Mark this task as done
            System.out.println((Duke.listOfTask).get(pos));
            Duke.HorizontalLine();
        }
    },
    TODO {
        @Override
        public void execute(String s1) {
            String[] arr = s1.split("\\s+", 2);
            Duke.add(new Todo(arr[1]));
        }
    },
    DEADLINE {
        @Override
        public void execute(String s1) {
            // Manipulating the String by separating the actual command
            // and the word '/by' to get the description and date/time
            int limit = s1.lastIndexOf("/by") - 1;
            String[] arr = s1.split("\\s+", 2);
            int dateindex = arr[1].lastIndexOf("/by");
            String substr = arr[1].substring(dateindex);
            String[] strarr = substr.split("\\s+", 2);

            DateTimeFormatter formatdate = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            // Translating input date to the form of "MM d yyyy"
            String[] inputdate = (strarr[1]).split("\\s+", 2);

            try {
                LocalDate date = LocalDate.parse(inputdate[0],
                        formatdate.withResolverStyle(ResolverStyle.STRICT));

                // Format it to english. For example, 3 Oct 2019
                inputdate[0] = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));


                // Translating the time from 24-hour format to
                // AM/PM format
                if ((inputdate[1].toCharArray()).length != 4)
                    throw new Exception("Please enter time in the form of 24 hour format!");
                int dateInt = Integer.parseInt(inputdate[1]);
                int timeTest = dateInt % 100;
                if ((timeTest > 60 || timeTest < 0) || (dateInt > 2359 || dateInt < 0))
                    throw new Exception("Invalid time!");

                String time = "";

                if (dateInt < 1300){
                    time = timeTest < 10 ? time + "0" : time;

                    String hour = dateInt < 100 ? "12" : Integer.toString(dateInt / 100);
                    String minute = Integer.toString(timeTest);

                    time = dateInt < 1200 ? hour + ":" + time + minute + "am" :
                            hour + ":" + time + minute + "pm";
                } else {
                    time = (dateInt % 100) < 10 ? time + "0" : time;
                    String hour = Integer.toString((dateInt / 100) - 12);
                    String minute = Integer.toString(timeTest);
                    time = hour + ":" + time + minute + "pm";
                }
                inputdate[1] = time;

                String newstr = String.join(" ", inputdate);
                Duke.add(new Deadline(s1.substring(9, limit), newstr));
            } catch (DateTimeParseException exception){
                System.out.println(exception.getMessage());
                System.out.println("Print date in the form of yyy-mm-dd");
                return;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
    },
    EVENT {
        public void execute(String s1){
            // Manipulating the String by separating the actual command
            // and the word '/at' to get the description and date/time
            int limit = s1.lastIndexOf("/at") - 1;
            String[] arr = s1.split("\\s+", 2);
            int dateindex = arr[1].lastIndexOf("/at");
            String substr = arr[1].substring(dateindex);
            String[] strarr = substr.split("\\s+", 2);

            DateTimeFormatter formatdate = DateTimeFormatter.ofPattern("uuuu-MM-dd");
            // Translating input date to the form of "MM d yyyy"
            String[] inputdate = (strarr[1]).split("\\s+", 2);

            try {
                LocalDate date = LocalDate.parse(inputdate[0],
                        formatdate.withResolverStyle(ResolverStyle.STRICT));

                // Format it to english. For example, 3 Oct 2019
                inputdate[0] = date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));


                // Translating the time from 24-hour format to
                // AM/PM format
                if ((inputdate[1].toCharArray()).length != 4)
                    throw new Exception("Please enter time in the form of 24 hour format!");
                int dateInt = Integer.parseInt(inputdate[1]);
                int timeTest = dateInt % 100;
                if ((timeTest > 60 || timeTest < 0) || (dateInt > 2359 || dateInt < 0))
                    throw new Exception("Invalid time!");

                String time = "";

                if (dateInt < 1300){
                    time = timeTest < 10 ? time + "0" : time;

                    String hour = dateInt < 100 ? "12" : Integer.toString(dateInt / 100);
                    String minute = Integer.toString(timeTest);
                    time = dateInt < 1200 ? hour + ":" + time + minute + "am" :
                            hour + ":" + time + minute + "pm";
                } else {
                    time = (dateInt % 100) < 10 ? time + "0" : time;
                    String hour = Integer.toString((dateInt / 100) - 12);
                    String minute = Integer.toString(timeTest);
                    time = hour + ":" + time + minute + "pm";
                }
                inputdate[1] = time;

                String newstr = String.join(" ", inputdate);
                Duke.add(new Event(s1.substring(6, limit), newstr));
            } catch (DateTimeParseException exception){
                System.out.println(exception.getMessage());
                System.out.println("Print date in the form of yyy-mm-dd");
                return;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }

        }
    },
    DELETE {
        @Override
        public void execute(String s1) {
            // Split the string to get the
            // index of the task to be deleted
            String[] arrdel = s1.split("\\s+");
            int pos = Integer.parseInt(arrdel[1]) - 1;

            Duke.HorizontalLine();
            System.out.println("Noted. I've removed this task:");
            System.out.println((Duke.listOfTask).get(pos));
            (Duke.listOfTask).remove(pos); // Mark this task as done

            System.out.println("Now you have " + Duke.listOfTask.size() + " tasks in the list.");
            Duke.HorizontalLine();
        }
    };

    public abstract void execute(String s1);
}