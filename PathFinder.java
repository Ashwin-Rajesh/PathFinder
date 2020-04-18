import java.util.*;

public class PathFinder
{
    private World world;
    private PriorityQueue<cell> openSet = new PriorityQueue<cell>();
    private Set<cell> closedSet = new HashSet<>();

    public PathFinder()
    {
        world = new World(8, 8);
    }

    public static void main(String[] args) 
    {
        PathFinder p = new PathFinder();

        Scanner input = new Scanner(System.in);
        String init = "", fin = "";
        Shape shape = new Shape();
        Shape.pickAndFill(shape);
        int x = 1 + (int) (Math.random() * (7 - shape.getWidth()));
        int y = 1 + (int) (Math.random() * (7 - shape.getHeight()));
        p.world.setObstacle(shape, x, y);

        p.world.print(null);

        do
        {
            System.out.println("\nEnter the initial point (A0/a0)");
            init = input.nextLine();
            System.out.println("Enter the final point (H7/h7)");
            fin = input.nextLine();
            System.out.println();
        }
        while (!(p.world.setInitialPoint(init) && p.world.setFinalPoint(fin)));

        System.out.println("Inputs accepted!\n");

        p.solution();
        input.close();
    }

    public void solution()
    {
        cell c = world.getInitialPoint();
        c.setPathCost(0);
        c.setPrev(null);
        while (c.getName().compareTo(world.getFinalPoint().getName()) != 0)
        {
            for (cell n : c.getNeighbours())
            {
                if (!closedSet.contains(n))
                {
                    n.setPathCost(c.getPathCost() + 1);
                    n.setPrev(c);
                    openSet.add(n);
                }
            }
            closedSet.add(c);
            c = openSet.poll();
        }

        cell temp;
        temp = world.getFinalPoint();
        ArrayList<String> str = new ArrayList<String>();
        while (temp != null)
        {
            str.add(temp.getName());
            temp = temp.getPrev();
        }
        System.out.print(str.get(str.size() - 1));
        for (int i = str.size() - 2; i >= 0; i--)
        {
            System.out.print("->" + str.get(i)); // temp.getManhattanDistance()+ "-" + temp.getPathCost());
        }
        System.out.println();
        world.print(str);
    }
}

class World
{
    public cell[][] Cells;
    public cell initialPoint;
    public cell finalPoint;

    public World(int height, int width)
    {
        Cells = new cell[height][width];

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                Cells[i][j] = new cell((char) (i + 65) + Integer.toString(j), new int[] { i, j });
            }
        }

        for (int i = 0; i < height; i++)
        {
            for (int j = 0; j < width; j++)
            {
                if (i != 0)
                    Cells[i][j].addNeighbour(Cells[i - 1][j]);
                if (i != 7)
                    Cells[i][j].addNeighbour(Cells[i + 1][j]);
                if (j != 0)
                    Cells[i][j].addNeighbour(Cells[i][j - 1]);
                if (j != 7)
                    Cells[i][j].addNeighbour(Cells[i][j + 1]);
            }
        }
    }

    public void print(ArrayList str)
    {
        System.out.println();
        for (int i = 0; i < Cells.length; i++)
        {
            for (cell c : Cells[i])
            {
                if (str != null)
                {
                    if (c.isObstacle())
                        System.out.print(" () ");
                    else if (str.contains(c.getName()))
                        System.out.print(" __ ");
                    else
                        System.out.print(" " + c.getName() + " ");
                } 
                else
                {
                    if (c.isObstacle())
                        System.out.print(" () ");
                    else
                        System.out.print(" " + c.getName() + " ");
                }
            }
            System.out.println();
        }
    }

    public void setObstacle(Shape s, int x, int y)
    {
        boolean blocks[][] = s.getBlocks();
        for (int i = 0; i < s.getHeight(); i++)
            for (int j = 0; j < s.getWidth(); j++)
            {
                if (blocks[i][j])
                    Cells[i + y][j + x].makeObstacle();
            }
    }

    public boolean setInitialPoint(String init)
    {
        init = init.toLowerCase();
        if (init.length() != 2)
        {
            System.out.println("Invalid Entry");
            return false;
        }

        int i = init.charAt(0) - 97;
        int j = Character.getNumericValue(init.charAt(1));

        if (j < 0 || j >= Cells.length)
        {
            System.out.printf(" The given point " + init + " is out of bounds.\n\n");
            return false;
        }
        if (i < 0 || i >= Cells.length)
        {
            System.out.printf(" The given point " + init + " is out of bounds.\n\n");
            return false;
        }
        if (Cells[i][j].isObstacle())
        {
            System.out.printf(" The given point " + init + " is an obstacle.\n\n");
            return false;
        }

        initialPoint = Cells[i][j];
        return true;
    }

    public boolean setFinalPoint(String fin)
    {
        fin = fin.toLowerCase();
        if (fin.length() != 2)
        {
            System.out.println("Invalid Entry");
            return false;
        }

        int i = fin.charAt(0) - 97;
        int j = Character.getNumericValue(fin.charAt(1));

        if (j < 0 || j >= Cells.length)
        {
            System.out.printf(" The given point " + fin + " is out of bounds.\n\n");
            return false;
        }
        if (i < 0 || i >= Cells.length)
        {
            System.out.printf(" The given point " + fin + " is out of bounds.\n\n");
            return false;
        }
        if (Cells[i][j].isObstacle())
        {
            System.out.printf(" The given point " + fin + " is an obstacle.\n\n");
            return false;
        }

        finalPoint = Cells[i][j];
        cell.setFinalPoint(finalPoint);
        return true;
    }

    public cell getInitialPoint()
    {
        return initialPoint;
    }

    public cell getFinalPoint() 
    {
        return finalPoint;
    }
}

class cell implements Comparable<cell>
{
    private String name;
    private Vector<cell> neighbours;
    private int coordinate[];
    private cell prev;
    private int pathcost;
    private static cell finalpoint;

    public cell(String str, int arr[])
    {
        coordinate = arr.clone();
        neighbours = new Vector<cell>();
        name = str;
        prev = null;
    }

    public String getName()
    {
        return name;
    }

    public Vector<cell> getNeighbours()
    {
        return neighbours;
    }

    public int[] getCoordinates()
    {
        return coordinate;
    }

    public cell getPrev()
    {
        return prev;
    }

    public int getPathCost()
    {
        return pathcost;
    }

    public void setNeighbours(Vector<cell> vec)
    {
        neighbours = new Vector<cell>(vec);
    }

    public void setPrev(cell c)
    {
        prev = c;
    }

    public void setPathCost(int cost)
    {
        pathcost = cost;
    }

    public static void setFinalPoint(cell c) 
    {
        finalpoint = c;
    }

    public void addNeighbour(cell c) 
    {
        neighbours.add(c);
    }

    public void makeObstacle() 
    {
        for (cell c : neighbours)
            c.removeNeighbour(this);
        neighbours = null;
    }

    public boolean isObstacle() 
    {
        return neighbours == null;
    }

    public void removeNeighbour(cell c) 
    {
        neighbours.remove(c);
    }

    public int getManhattanDistance() 
    {
        return Math.abs(finalpoint.coordinate[0] - this.coordinate[0])
                + Math.abs(finalpoint.coordinate[1] - this.coordinate[1]);
    }

    @Override
    public int compareTo(cell that) 
    {
        if (this.pathcost > that.pathcost)
            return 1;
        else if (this.pathcost < that.pathcost)
            return -1;
        else
            return this.getManhattanDistance() - that.getManhattanDistance();
    }
}

class Shape 
{
    private int height;
    private int width;
    private boolean[][] blocks;

    public void setHeight(int height) 
    {
        this.height = height;
    }

    public void setWidth(int width) 
    {
        this.width = width;
    }

    public int getHeight() 
    {
        return height;
    }

    public int getWidth() 
    {
        return width;
    }

    public void setBlockade(boolean[][] filledBlocks) 
    {
        blocks = new boolean[height][width];
        for (int i = 0; i < height; i++) 
        {
            for (int j = 0; j < width; j++) 
            {
                blocks[i][j] = filledBlocks[i][j];
            }
        }
    }

    public boolean[][] getBlocks() 
    {
        return blocks;
    }

    public static void pickAndFill(Shape shape) 
    {
        int r = (int) (Math.random() * 3);
        boolean[][] filledBlocks = null;
        shape.setWidth(3);
        shape.setHeight(3);
        if (r == 0)// shapeT
        {
            filledBlocks = new boolean[3][3];
            for (int i = 0; i < shape.getHeight(); i++)
                for (int j = 0; j < shape.getWidth(); j++)
                    filledBlocks[i][j] = false;

            filledBlocks[0][0] = true;
            filledBlocks[0][1] = true;
            filledBlocks[0][2] = true;
            filledBlocks[1][1] = true;
            filledBlocks[2][1] = true;
        } 
        else if (r == 1)// shapeI
        {
            filledBlocks = new boolean[3][3];
            for (int i = 0; i < shape.getHeight(); i++)
                for (int j = 0; j < shape.getWidth(); j++)
                    filledBlocks[i][j] = false;

            filledBlocks[0][0] = true;
            filledBlocks[0][1] = true;
            filledBlocks[0][2] = true;
            filledBlocks[1][1] = true;
            filledBlocks[2][0] = true;
            filledBlocks[2][1] = true;
            filledBlocks[2][2] = true;
        } 
        else// shapeL
        {
            filledBlocks = new boolean[3][3];
            for (int i = 0; i < shape.getHeight(); i++)
                for (int j = 0; j < shape.getWidth(); j++)
                    filledBlocks[i][j] = false;

            filledBlocks[0][0] = true;
            filledBlocks[1][0] = true;
            filledBlocks[2][0] = true;
            filledBlocks[2][1] = true;
            filledBlocks[2][2] = true;
        }
        shape.setBlockade(filledBlocks);
    }
}
