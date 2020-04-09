package capers;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import edu.neu.ccs.XString;

import java.io.File;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

/** Represents a dog that can be serialized.
 * @author Sean Dooher
*/
public class Dog {

    /** Folder that dogs live in. */
    static final File DOG_FOLDER =  new File(".capers/dogs");

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        _age = age;
        _breed = breed;
        _name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        String fileName = name + ".txt";
        File[] allFile = DOG_FOLDER.listFiles();
        if (allFile == null) {
            return null;
        } else {
            for (File file : allFile) {
                if (file.getName().equals(fileName)) {
                    ArrayList<String> dog = Utils.readObject(file, ArrayList.class);
                    return new Dog(dog.get(0), dog.get(1),
                            Integer.parseInt(dog.get(2)));
                }
            }
        }
        return null;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        _age += 1;
        System.out.println(toString());
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        ArrayList<String> info = new ArrayList<String>();
        info.add(_name);
        info.add(_breed);
        info.add(String.valueOf(_age));
        File dog = new File(".capers/dogs/" + _name + ".txt");
        Utils.writeObject(dog, info);
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            _name, _breed, _age);
    }

    /** Age of dog. */
    private int _age;
    /** Breed of dog. */
    private String _breed;
    /** Name of dog. */
    private String _name;
}
