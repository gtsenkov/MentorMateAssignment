import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        int width = scan.nextInt();
        int length = scan.nextInt();
        scan.nextLine();

        //#From Assigment - if we have wall smaller than or equal to one brick there will be no solution.
        if (validateBrickQuantity(width, length)) return;

        //#From Assigment - if the width or length of the wall if bigger than 100 there will be no solution.
        if (validateWallSize(width, length)) return;

        //#My validation If wall length is odd number means not consistent brick, even though input should be correct.
        if (validateBrickConsistency(width, length)) return;

        //given input wall building
        int[][] brickWall1 = new int[width][length];

        //input reading
        if (inputReading(scan, width, length, brickWall1)) return;

        //#From Assigment - brick length validation
        if (validateBrickSpan(width, length, brickWall1)) return;

        //wall size
        int fields = width * length;

        //bricks in the wall
        int bricksNumbers = fields / 2;

        //random class needed for wall 2 solution
        Random random = new Random();

        //List class for all numbers for the wall
        List<Integer> nums = new ArrayList<>();

        //filling all numbers needed for wall 2. Be aware that this approach rely on consecutive numbers for the brick
        // in the wall as it is shown in the given examples. If bricks are not consecutive numbers todo wall 1 brick numbers gathering.
        for (int i = 1; i <= bricksNumbers; i++) {
            nums.add(i);
        }

        //variable for interim solution with randomized orientation and sequence.
        int[][] brickWall2 = new int[width][length];

        //generate randomized solution
        generateRandomizedSolution(width, length, brickWall1, random, nums, brickWall2);

        //intermediate check for solution accuracy.
        //printMatrix(brickWall2);

        //self sizing variable used as matrix from for final printable solution
        List<List<String>> wallPicture = new ArrayList<>();

        //adding first empty row from the matrix
        wallPicture.add(new ArrayList<>());

        //Creates wall with stars between the bricks and not more than 1 star between 2 adjacent bricks.
        //If you want you can make symbol between the bricks changeable by method refactoring.
        wallCreationWithSymbolsInBetween(width, length, brickWall2, wallPicture);

        //Prints the final solution
        printListMatrix(wallPicture);


    }

    private static void wallCreationWithSymbolsInBetween(int width, int length, int[][] brickWall2, List<List<String>> wallPicture) {
        for (int row = 0; row < width; ) {

            //check first and every odd row from the matrix and fills the picture
            for (int evenRow = 0; evenRow < 1; evenRow++, row++) {
                wallPicture.add(new ArrayList<>());
                for (int col = 0; col < length; ) {

                    if (row == 0) {
                        wallPicture.get(row).add(("" + brickWall2[row][col]));

                        if (length - col > 1 && brickWall2[row][col] == brickWall2[row][col + 1]) {
                            wallPicture.get(row).add(("" + brickWall2[row][col]));
                            wallPicture.get(row).add("*");
                            wallPicture.get(row + 1).add("*");
                            wallPicture.get(row + 1).add("*");
                            wallPicture.get(row + 1).add("*");
                            col += 2;

                        } else {
                            if (width - row > 1) {
                                if (brickWall2[row][col] == brickWall2[row + 1][col]) {
                                    wallPicture.get(row + 1).add(("" + brickWall2[row + 1][col]));
                                    wallPicture.get(row).add("*");
                                    wallPicture.get(row + 1).add("*");
                                    col++;
                                }
                            }
                        }

                    } else {
                        wallPicture.add(new ArrayList<>());

                        wallPicture.get(row + 1).add(("" + brickWall2[row][col]));

                        if (length - col > 1 && brickWall2[row][col] == brickWall2[row][col + 1]) {
                            wallPicture.get(row + 1).add(("" + brickWall2[row][col]));
                            wallPicture.get(row + 1).add("*");
                            wallPicture.get(row + 2).add("*");
                            wallPicture.get(row + 2).add("*");
                            wallPicture.get(row + 2).add("*");
                            col += 2;

                        } else {
                            if (width - row > 1) {
                                if (brickWall2[row][col] == brickWall2[row + 1][col]) {
                                    wallPicture.get(row + 2).add(("" + brickWall2[row + 1][col]));
                                    wallPicture.get(row + 1).add("*");
                                    wallPicture.get(row + 1 + 1).add("*");
                                    col++;
                                }
                            }
                        }
                    }

                }
            }


            //check second and every even row from the matrix and fills the picture
            for (int oddRow = 1; oddRow < 2; oddRow++, row++) {
                wallPicture.add(new ArrayList<>());
                for (int col = 0; col < length; ) {
                    if (row < 3) {
                        if (brickWall2[row][col] == brickWall2[row - 1][col]) {
                            wallPicture.get(row + 1).add("*");
                            wallPicture.get(row + 1).add("*");
                            col++;
                        } else {
                            wallPicture.get(row + 1).add(("" + brickWall2[row][col]));
                            wallPicture.get(row + 1).add(("" + brickWall2[row][col]));
                            wallPicture.get(row + 1).add("*");
                            col += 2;
                        }

                    } else {
                        if (brickWall2[row][col] == brickWall2[row - 1][col]) {
                            wallPicture.get(row + 2).add("*");
                            wallPicture.get(row + 2).add("*");
                            col++;
                        } else {
                            wallPicture.get(row + 2).add(("" + brickWall2[row][col]));
                            wallPicture.get(row + 2).add(("" + brickWall2[row][col]));
                            wallPicture.get(row + 2).add("*");
                            col += 2;
                        }
                    }
                }
            }

        }
    }

    private static void generateRandomizedSolution(int width, int length, int[][] brickWall1, Random random, List<Integer> nums, int[][] brickWall2) {
        for (int row = 0; row < width && nums.size() != 0; row++) {
            for (int col = 0; col < length && nums.size() != 0; ) {
                //variable which is prepared choose random index from brick numbers collection
                int randomIndex = random.nextInt(nums.size());
                //variable which stores random brick number from input brick collection
                int randomNumber = nums.get(randomIndex);
                // variable intended to store default brick orientation
                int randomOrientation = 1;
                if (row != width - 1) {
                    randomOrientation = random.nextInt(2);
                }

                //variable used to double validate if there is space for random oriented brick
                boolean isAdded = false;
                if (brickWall2[row][col] == 0) {

                    if (randomOrientation == 0 && row % 2 == 0 && brickWall2[row + 1][col] == 0 && row + 1 < width && randomNumber != brickWall1[row][col] && randomNumber != brickWall1[row + 1][col]) {
                        isAdded = true;
                        brickWall2[row][col] = randomNumber;
                        brickWall2[row + 1][col] = randomNumber;
                        col++;
                    } else if (col + 1 < length && brickWall2[row][col + 1] == 0 && randomNumber != brickWall1[row][col] && randomNumber != brickWall1[row][col + 1]) {
                        isAdded = true;
                        brickWall2[row][col] = randomNumber;
                        brickWall2[row][col + 1] = randomNumber;
                        col += 2;
                    }
                } else {
                    col++;
                }

                if (isAdded) {
                    nums.remove(Integer.valueOf(randomNumber));
                }
            }
        }
    }

    private static boolean validateBrickSpan(int width, int length, int[][] brickWall1) {
        for (int row = 0; row < width; row++) {
            for (int col = 0; col < length; col++) {
                if (length > 2 && length - col > 2) {
                    if (brickWall1[row][col] == brickWall1[row][col + 1] && brickWall1[row][col] == brickWall1[row][col + 2]) {
                        System.out.println("Brick with number " + brickWall1[row][col] + " is too long!");
                        return true;
                    }
                }
                if (width > 2 && width - row > 2) {
                    if (brickWall1[row][col] == brickWall1[row + 1][col] && brickWall1[row][col] == brickWall1[row + 2][col]) {
                        System.out.println("Brick with number " + brickWall1[row][col] + " is too long!");
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean inputReading(Scanner scan, int width, int length, int[][] brickWall1) {
        for (int row = 0; row < width; row++) {

            //variable which stores the input on each line
            String inputLine = scan.nextLine();

            if (inputLine.equals("")) {
                System.out.println("Insufficient bricks for the given wall width!");
                return true;
                //program will wait for line of input and if empty line is entered - this will be the case.
            }

            //array which collects the input on each line
            int[] inputs = Arrays.stream(inputLine.split("\\s+"))
                    .mapToInt(Integer::parseInt).toArray();
            if (inputs.length < length) {
                System.out.println("Insufficient bricks for the given length!");
                return true;
            } else if (inputs.length > length) {
                System.out.println("Bricks are too much for the given wall length!");
                return true;
            }

            //if there is more lines of input this won't be read.
            brickWall1[row] = inputs;

        }
        return false;
    }

    private static boolean validateBrickConsistency(int width, int length) {
        if (width % 2 == 1 || length % 2 == 1) {
            System.out.println("Broken brick!");
            return true;
        }
        return false;
    }

    private static boolean validateWallSize(int width, int length) {
        if (width >= 100 || length >= 100) {
            System.out.println("Wall is too big!");
            return true;
        }
        return false;
    }

    private static boolean validateBrickQuantity(int width, int length) {
        if (width <= 2 && length <= 0 || length <= 2 && width <= 0) {
            System.out.println(" -1. Solution does not exist!");
            return true;
        }
        return false;
    }

    public static void printListMatrix(List<List<String>> matrix) {
        for (int row = 0; row < matrix.size(); row++) {
            for (int col = 0; col < matrix.get(row).size(); col++) {
                System.out.print(matrix.get(row).get(col));
            }
            System.out.println();
        }
    }
}

//TODO if you do not see your output, please re-run the application with the same input it works most of the trials.
