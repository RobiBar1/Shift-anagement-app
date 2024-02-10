package com.example.androidproject.Users;
import java.io.Serializable;
import java.security.SecureRandom;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.util.Base64;


public class User implements Serializable {
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    private byte[] hashedPassword;
    private byte[] salt;
    private int iterations;
    private String role;
    private int numberOfShiftsWorkedThatMonth;

    public int getNumberOfShiftsWorkedThatMonth() {
        return numberOfShiftsWorkedThatMonth;
    }

    public void setNumberOfShiftsWorkedThatMonth(int numberOfShiftsWorkedThatMonth) {
        this.numberOfShiftsWorkedThatMonth = numberOfShiftsWorkedThatMonth;
    }

    private byte[] getHashedPassword() {
        return hashedPassword;
    }

    private void setHashedPassword(byte[] hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    private byte[] getSalt() {
        return salt;
    }

    private void setSalt(byte[] salt) {
        this.salt = salt;
    }

    private int getIterations() {
        return iterations;
    }

    private void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role) throws Exception
    {
        CheckRole(role);
        this.role = role;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) throws Exception, NumberFormatException
    {
        CheckId(id);
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) throws Exception
    {
        CheckFirstName(firstName);
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws Exception
    {
        CheckLastName(lastName);
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws Exception, NumberFormatException
    {
        CheckPhoneNumber(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) throws Exception
    {
        CheckEmail(email);
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws Exception
    {
        CheckPassword(password);
        this.password = generateSecurePasswordHash(password);
    }

    public void setUser(String id, String firstName, String lastName, String phoneNumber, String email, String password,
                        String role)
            throws Exception, NumberFormatException
    {
        setId(id);
        setFirstName(firstName);
        setLastName(lastName);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setPassword(password);
        setRole(role);
    }

    public User getUser()
    {
        return this;
    }

    public User(String id, String firstName, String lastName, String phoneNumber, String email, String password,
                String role)
            throws Exception, NumberFormatException
    {
        setUser(id, firstName, lastName, phoneNumber, email, password, role);
    }

    private void CheckRole(String role) throws Exception
    {
        if(role.isEmpty())
        {
            throw new Exception("Role can't be empty, enter your role");
        }
    }

    static public void CheckId(String id) throws NumberFormatException, Exception
    {
        if(id.length() != 9)
        {
            throw new Exception("Id should be 9 numbers");
        }

        Integer.parseInt(id);
    }

    private void CheckFirstName(String FirstName) throws Exception
    {
        if(FirstName.isEmpty())
        {
            throw new Exception("First name can't be empty, enter your name");
        }
    }

    private void CheckLastName(String LastName) throws Exception
    {
        if(LastName.isEmpty())
        {
            throw new Exception("Last name can't be empty, enter your last name");
        }
    }

    private void CheckPhoneNumber(String PhoneNumber) throws NumberFormatException, Exception
    {
        if(PhoneNumber.length() != 10)
        {
            throw new Exception("Phone number should be 10 numbers");
        }

        Integer.parseInt(PhoneNumber);
    }

    private void CheckEmail(String Email) throws Exception
    {
        if(Email.isEmpty())
        {
            throw new Exception("Email can't be empty, enter your Email");
        }
        if(!Email.contains("@"))
        {
            throw new Exception("Email most contains a @, check your email adress again");
        }
    }

    static public void CheckPassword(String Password) throws Exception
    {
        if(Password.isEmpty())
        {
            throw new Exception("You most enter password");
        }
    }

    public boolean CheckIfPasswordIsCorrect(String password) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        return getPassword().equalsIgnoreCase(Base64.getEncoder().
                encodeToString(getSalt()) + "$" + Base64.getEncoder().
                encodeToString(hashPassword(password, getSalt(), getIterations())));
    }

    private String generateSecurePasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        setSalt(generateSalt());
        setIterations(10); // Adjust iteration count as needed
        setHashedPassword(hashPassword(password, getSalt(), getIterations()));

        // Combine salt and hashed password for storage
        return Base64.getEncoder().encodeToString(getSalt()) + "$" + Base64.getEncoder().encodeToString(getHashedPassword());
    }

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        byte[] salt = new byte[16]; // Adjust salt length as needed
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.nextBytes(salt);
        return salt;
    }

    private static byte[] hashPassword(String password, byte[] salt, int iterations) throws NoSuchAlgorithmException, InvalidKeySpecException {
        char[] passwordChars = password.toCharArray();
        PBEKeySpec keySpec = new PBEKeySpec(passwordChars, salt, iterations, 64 * 2); // 512-bit key
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return keyFactory.generateSecret(keySpec).getEncoded();
    }
}
