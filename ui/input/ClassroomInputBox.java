package ui.input;

import model.classroom.SimpleClassroom;
import model.database.ClassroomDatabase;

import javax.swing.*;
import java.util.List;

/**
 * Created by hyice on 5/3/14.
 */
public class ClassroomInputBox extends JComboBox{

    private List<SimpleClassroom> classroomList;

    public ClassroomInputBox(int x, int y) {

        super();
        setBounds(x, y, 180, 40);
        setEditable(false);

        refreshData();
    }

    public ClassroomInputBox(int x, int y, int width, int height) {

        super();
        setBounds(x, y, width, height);
        setEditable(false);

        refreshData();
    }

    public void refreshData() {

        removeAllItems();

        classroomList = ClassroomDatabase.getSimpleClassroomList();
        for(SimpleClassroom classroom : classroomList) {

            addItem(classroom.getName());
        }
    }

    public int getSelctedCid() {

        return classroomList.get(getSelectedIndex()).getCid();
    }

    public String getSelectedName() {

        return (String)getSelectedItem();
    }

}
