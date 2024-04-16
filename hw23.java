import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

public class hw23 extends JFrame {
    private JTextField nameField, phoneField;
    private DefaultListModel<String> contactListModel;
    private JList<String> contactList;

    private ArrayList<Contact> contacts;

    public hw23() {
        setTitle("Contact Manager");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contacts = new ArrayList<>();
        loadContacts(); 

        JPanel panel = new JPanel(new GridLayout(3, 2));
        nameField = new JTextField();
        phoneField = new JTextField();
        JButton addButton = new JButton("Add Contact");
        JButton deleteButton = new JButton("Delete Contact");
        JButton editButton = new JButton("Edit Contact");
        contactListModel = new DefaultListModel<>();
        contactList = new JList<>(contactListModel);

        for (Contact contact : contacts) {
            contactListModel.addElement(contact.getName() + " - " + contact.getPhone());
        }

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                Contact newContact = new Contact(name, phone);
                contacts.add(newContact);
                contactListModel.addElement(newContact.getName() + " - " + newContact.getPhone());
                saveContacts();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    contacts.remove(selectedIndex);
                    contactListModel.remove(selectedIndex);
                    saveContacts();
                }
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = contactList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String newName = nameField.getText();
                    String newPhone = phoneField.getText();
                    Contact editedContact = new Contact(newName, newPhone);
                    contacts.set(selectedIndex, editedContact);
                    contactListModel.set(selectedIndex, editedContact.getName() + " - " + editedContact.getPhone());
                    saveContacts();
                }
            }
        });

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(addButton);
        panel.add(deleteButton);
        panel.add(editButton);

        JScrollPane scrollPane = new JScrollPane(contactList);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    private void saveContacts() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("contacts.txt"))) {
            for (Contact contact : contacts) {
                writer.println(contact.getName() + "," + contact.getPhone());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContacts() {
        try (BufferedReader reader = new BufferedReader(new FileReader("contacts.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    contacts.add(new Contact(parts[0], parts[1]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new hw23();
            }
        });
    }

    private static class Contact {
        private String name;
        private String phone;

        public Contact(String name, String phone) {
            this.name = name;
            this.phone = phone;
        }

        public String getName() {
            return name;
        }

        public String getPhone() {
            return phone;
        }
    }
}
