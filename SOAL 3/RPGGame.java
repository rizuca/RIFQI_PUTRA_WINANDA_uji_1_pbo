import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Interface untuk berbagai skill yang bisa dimiliki karakter
interface Skill {
    /**
     * Method untuk menggunakan skill
     * @param target karakter target (bisa null untuk self-buff)
     * @return damage atau healing yang dihasilkan
     */
    int useSkill(Character target);
    
    /**
     * Method untuk mendapatkan nama skill
     */
    String getSkillName();
    
    /**
     * Method untuk mendapatkan deskripsi skill
     */
    String getDescription();
    
    /**
     * Method untuk mendapatkan mana cost
     */
    int getManaCost();
    
    /**
     * Method untuk mengecek cooldown
     */
    boolean isOnCooldown();
    
    /**
     * Method untuk reset cooldown
     */
    void resetCooldown();
}

// Abstract class Character sebagai parent untuk semua karakter
abstract class Character {
    // Protected agar bisa diakses oleh subclass
    protected String name;
    protected int level;
    protected int health;
    protected int maxHealth;
    protected int mana;
    protected int maxMana;
    protected int attack;
    protected int defense;
    protected int experience;
    protected List<Skill> skills;
    protected boolean isAlive;
    
    // Constructor untuk inisialisasi karakter dasar
    public Character(String name, int health, int mana, int attack, int defense) {
        this.name = name;
        this.level = 1;
        this.health = health;
        this.maxHealth = health;
        this.mana = mana;
        this.maxMana = mana;
        this.attack = attack;
        this.defense = defense;
        this.experience = 0;
        this.skills = new ArrayList<>();
        this.isAlive = true;
    }
    
    // Method abstract yang harus diimplementasikan oleh setiap subclass
    public abstract void useSpecialAbility(Character target);
    public abstract String getCharacterClass();
    public abstract void levelUp();
    
    // Method untuk basic attack
    public int basicAttack(Character target) {
        if (!this.isAlive || !target.isAlive) {
            System.out.println("Cannot attack - one of the characters is dead");
            return 0;
        }
        
        Random rand = new Random();
        int damage = this.attack + rand.nextInt(10) - target.defense;
        damage = Math.max(1, damage); // Minimum 1 damage
        
        target.takeDamage(damage);
        System.out.println(this.name + " attacks " + target.name + " for " + damage + " damage");
        
        return damage;
    }
    
    // Method untuk menerima damage
    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health <= 0) {
            this.health = 0;
            this.isAlive = false;
            System.out.println(this.name + " has been defeated!");
        }
    }
    
    // Method untuk healing
    public void heal(int amount) {
        this.health += amount;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
        System.out.println(this.name + " heals for " + amount + " HP");
    }
    
    // Method untuk menggunakan mana
    public boolean useMana(int cost) {
        if (this.mana >= cost) {
            this.mana -= cost;
            return true;
        }
        System.out.println(this.name + " doesn't have enough mana");
        return false;
    }
    
    // Method untuk restore mana
    public void restoreMana(int amount) {
        this.mana += amount;
        if (this.mana > this.maxMana) {
            this.mana = this.maxMana;
        }
    }
    
    // Method untuk menambah skill
    public void addSkill(Skill skill) {
        this.skills.add(skill);
        System.out.println(this.name + " learned " + skill.getSkillName());
    }
    
    // Method untuk menggunakan skill berdasarkan index
    public void useSkill(int skillIndex, Character target) {
        if (skillIndex < 0 || skillIndex >= skills.size()) {
            System.out.println("Invalid skill index");
            return;
        }
        
        Skill skill = skills.get(skillIndex);
        if (skill.isOnCooldown()) {
            System.out.println(skill.getSkillName() + " is on cooldown");
            return;
        }
        
        if (useMana(skill.getManaCost())) {
            int result = skill.useSkill(target);
            System.out.println(this.name + " uses " + skill.getSkillName() + 
                             (result > 0 ? " dealing " + result + " damage/healing" : ""));
        }
    }
    
    // Method untuk menampilkan status karakter
    public void displayStatus() {
        System.out.println("=== " + name + " (" + getCharacterClass() + ") ===");
        System.out.println("Level: " + level);
        System.out.println("Health: " + health + "/" + maxHealth);
        System.out.println("Mana: " + mana + "/" + maxMana);
        System.out.println("Attack: " + attack);
        System.out.println("Defense: " + defense);
        System.out.println("Experience: " + experience);
        System.out.println("Status: " + (isAlive ? "Alive" : "Dead"));
        
        if (!skills.isEmpty()) {
            System.out.println("Skills:");
            for (int i = 0; i < skills.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + skills.get(i).getSkillName() + 
                                 " (Cost: " + skills.get(i).getManaCost() + " MP)");
            }
        }
    }
    
    // Method untuk menambah experience
    public void gainExperience(int exp) {
        this.experience += exp;
        System.out.println(this.name + " gains " + exp + " experience");
        
        // Check for level up
        if (this.experience >= this.level * 100) {
            levelUp();
        }
    }
    
    // Getter methods
    public String getName() { return name; }
    public int getLevel() { return level; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }
    public int getMana() { return mana; }
    public int getMaxMana() { return maxMana; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public boolean isAlive() { return isAlive; }
    public List<Skill> getSkills() { return skills; }
}

// Implementasi skill untuk Warrior
class PowerStrike implements Skill {
    private boolean onCooldown = false;
    
    @Override
    public int useSkill(Character target) {
        Random rand = new Random();
        int damage = 50 + rand.nextInt(20);
        if (target != null) {
            target.takeDamage(damage);
        }
        onCooldown = true;
        return damage;
    }
    
    @Override
    public String getSkillName() { return "Power Strike"; }
    
    @Override
    public String getDescription() { return "A powerful attack that deals massive damage"; }
    
    @Override
    public int getManaCost() { return 20; }
    
    @Override
    public boolean isOnCooldown() { return onCooldown; }
    
    @Override
    public void resetCooldown() { onCooldown = false; }
}

class DefensiveStance implements Skill {
    private boolean onCooldown = false;
    
    @Override
    public int useSkill(Character target) {
        // Self-buff skill
        if (target != null) {
            // Temporary defense boost (simplified)
            System.out.println(target.getName() + " enters defensive stance, defense increased");
        }
        onCooldown = true;
        return 0;
    }
    
    @Override
    public String getSkillName() { return "Defensive Stance"; }
    
    @Override
    public String getDescription() { return "Increases defense temporarily"; }
    
    @Override
    public int getManaCost() { return 15; }
    
    @Override
    public boolean isOnCooldown() { return onCooldown; }
    
    @Override
    public void resetCooldown() { onCooldown = false; }
}

// Implementasi skill untuk Mage
class Fireball implements Skill {
    private boolean onCooldown = false;
    
    @Override
    public int useSkill(Character target) {
        Random rand = new Random();
        int damage = 40 + rand.nextInt(25);
        if (target != null) {
            target.takeDamage(damage);
            System.out.println("Fireball burns " + target.getName());
        }
        onCooldown = true;
        return damage;
    }
    
    @Override
    public String getSkillName() { return "Fireball"; }
    
    @Override
    public String getDescription() { return "Launches a burning fireball at the enemy"; }
    
    @Override
    public int getManaCost() { return 25; }
    
    @Override
    public boolean isOnCooldown() { return onCooldown; }
    
    @Override
    public void resetCooldown() { onCooldown = false; }
}

class Heal implements Skill {
    private boolean onCooldown = false;
    
    @Override
    public int useSkill(Character target) {
        Random rand = new Random();
        int healing = 30 + rand.nextInt(20);
        if (target != null) {
            target.heal(healing);
        }
        onCooldown = true;
        return healing;
    }
    
    @Override
    public String getSkillName() { return "Heal"; }
    
    @Override
    public String getDescription() { return "Restores health to target"; }
    
    @Override
    public int getManaCost() { return 20; }
    
    @Override
    public boolean isOnCooldown() { return onCooldown; }
    
    @Override
    public void resetCooldown() { onCooldown = false; }
}

// Skill untuk Archer
class PrecisionShot implements Skill {
    private boolean onCooldown = false;
    
    @Override
    public int useSkill(Character target) {
        // Always hits, ignores some defense
        int damage = 35 + new Random().nextInt(15);
        if (target != null) {
            target.takeDamage(damage);
            System.out.println("Precision shot hits " + target.getName() + " perfectly");
        }
        onCooldown = true;
        return damage;
    }
    
    @Override
    public String getSkillName() { return "Precision Shot"; }
    
    @Override
    public String getDescription() { return "A precise shot that never misses"; }
    
    @Override
    public int getManaCost() { return 18; }
    
    @Override
    public boolean isOnCooldown() { return onCooldown; }
    
    @Override
    public void resetCooldown() { onCooldown = false; }
}

// Class Warrior - mewarisi Character
class Warrior extends Character {
    
    public Warrior(String name) {
        super(name, 120, 50, 25, 15); // High health, low mana, high attack
        
        // Add warrior-specific skills
        addSkill(new PowerStrike());
        addSkill(new DefensiveStance());
    }
    
    @Override
    public void useSpecialAbility(Character target) {
        System.out.println(this.name + " uses Berserker Rage!");
        this.attack += 10;
        this.defense -= 5;
        System.out.println("Attack increased, defense decreased");
    }
    
    @Override
    public String getCharacterClass() {
        return "Warrior";
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 20;
        this.health = this.maxHealth;
        this.maxMana += 5;
        this.mana = this.maxMana;
        this.attack += 5;
        this.defense += 3;
        this.experience = 0;
        
        System.out.println(this.name + " leveled up to level " + this.level + "!");
        System.out.println("Health and attack increased significantly");
    }
}

// Class Mage - mewarisi Character
class Mage extends Character {
    
    public Mage(String name) {
        super(name, 80, 100, 15, 8); // Low health, high mana, moderate attack
        
        // Add mage-specific skills
        addSkill(new Fireball());
        addSkill(new Heal());
    }
    
    @Override
    public void useSpecialAbility(Character target) {
        System.out.println(this.name + " casts Mana Shield!");
        System.out.println("Next attack will consume mana instead of health");
        // Implementation would involve temporary status effect
    }
    
    @Override
    public String getCharacterClass() {
        return "Mage";
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 10;
        this.health = this.maxHealth;
        this.maxMana += 20;
        this.mana = this.maxMana;
        this.attack += 3;
        this.defense += 2;
        this.experience = 0;
        
        System.out.println(this.name + " leveled up to level " + this.level + "!");
        System.out.println("Mana and magical power increased significantly");
    }
}

// Class Archer - mewarisi Character
class Archer extends Character {
    
    public Archer(String name) {
        super(name, 90, 70, 20, 10); // Balanced stats with focus on precision
        
        // Add archer-specific skills
        addSkill(new PrecisionShot());
    }
    
    @Override
    public void useSpecialAbility(Character target) {
        System.out.println(this.name + " activates Eagle Eye!");
        System.out.println("Next few attacks will have increased accuracy and critical chance");
    }
    
    @Override
    public String getCharacterClass() {
        return "Archer";
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 15;
        this.health = this.maxHealth;
        this.maxMana += 10;
        this.mana = this.maxMana;
        this.attack += 4;
        this.defense += 2;
        this.experience = 0;
        
        System.out.println(this.name + " leveled up to level " + this.level + "!");
        System.out.println("Agility and precision increased");
    }
}

// Singleton pattern untuk role eksklusif (menggantikan sealed class)
// Hanya boleh ada satu LegendaryKnight dalam game
class LegendaryKnight extends Character {
    private static LegendaryKnight instance = null;
    private static final Object lock = new Object();
    
    // Private constructor untuk mencegah instantiation langsung
    private LegendaryKnight(String name) {
        super(name, 200, 80, 35, 25); // Superior stats
        
        // Add legendary skills
        addSkill(new PowerStrike());
        addSkill(new DefensiveStance());
        addSkill(new Heal());
        
        System.out.println("A Legendary Knight has been awakened: " + name);
    }
    
    // Singleton pattern - hanya boleh ada satu instance
    public static LegendaryKnight getInstance(String name) {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new LegendaryKnight(name);
                } else {
                    System.out.println("A Legendary Knight already exists: " + instance.getName());
                    System.out.println("Cannot create another Legendary Knight");
                    return null;
                }
            }
        } else {
            System.out.println("A Legendary Knight already exists: " + instance.getName());
            return null;
        }
        return instance;
    }
    
    // Method untuk mengecek apakah sudah ada Legendary Knight
    public static boolean exists() {
        return instance != null && instance.isAlive();
    }
    
    // Method untuk mereset instance (jika knight mati)
    public static void resetInstance() {
        synchronized (lock) {
            instance = null;
            System.out.println("The Legendary Knight has fallen. A new one may arise.");
        }
    }
    
    @Override
    public void useSpecialAbility(Character target) {
        System.out.println(this.name + " channels the power of legends!");
        System.out.println("All stats temporarily doubled!");
        
        // Temporary massive stat boost
        int originalAttack = this.attack;
        int originalDefense = this.defense;
        this.attack *= 2;
        this.defense *= 2;
        
        System.out.println("Attack: " + originalAttack + " -> " + this.attack);
        System.out.println("Defense: " + originalDefense + " -> " + this.defense);
    }
    
    @Override
    public String getCharacterClass() {
        return "Legendary Knight";
    }
    
    @Override
    public void levelUp() {
        this.level++;
        this.maxHealth += 30;
        this.health = this.maxHealth;
        this.maxMana += 15;
        this.mana = this.maxMana;
        this.attack += 8;
        this.defense += 5;
        this.experience = 0;
        
        System.out.println(this.name + " leveled up to level " + this.level + "!");
        System.out.println("Legendary power grows stronger!");
    }
    
    @Override
    public void takeDamage(int damage) {
        super.takeDamage(damage);
        // Reset singleton if legendary knight dies
        if (!this.isAlive) {
            resetInstance();
        }
    }
}

// Class untuk mengelola party/group karakter
class Party {
    private List<Character> members;
    private String partyName;
    
    public Party(String partyName) {
        this.partyName = partyName;
        this.members = new ArrayList<>();
    }
    
    public void addMember(Character character) {
        if (character != null) {
            members.add(character);
            System.out.println(character.getName() + " joined party " + partyName);
        }
    }
    
    public void displayParty() {
        System.out.println("=== Party: " + partyName + " ===");
        if (members.isEmpty()) {
            System.out.println("No members in party");
            return;
        }
        
        for (int i = 0; i < members.size(); i++) {
            System.out.println((i + 1) + ". " + members.get(i).getName() + 
                             " (" + members.get(i).getCharacterClass() + ") - Level " + 
                             members.get(i).getLevel());
        }
    }
    
    public void displayAllStatus() {
        System.out.println("=== Full Party Status ===");
        for (Character member : members) {
            member.displayStatus();
            System.out.println();
        }
    }
    
    public List<Character> getMembers() {
        return members;
    }
    
    public Character getMember(int index) {
        if (index >= 0 && index < members.size()) {
            return members.get(index);
        }
        return null;
    }
}

// Main class untuk demonstrasi
public class RPGGame {
    public static void main(String[] args) {
        System.out.println("=== RPG CHARACTER SYSTEM ===");
        System.out.println("Demonstrating OOP concepts in game development");
        System.out.println();
        
        // Membuat party
        Party adventurers = new Party("The Brave Adventurers");
        
        // 1. Demonstrasi pembuatan karakter (Polymorphism)
        System.out.println("1. CREATING CHARACTERS:");
        System.out.println("========================");
        
        Character[] characters = {
            new Warrior("Thorin"),
            new Mage("Gandalf"),
            new Archer("Legolas")
        };
        
        // Menambahkan ke party
        for (Character character : characters) {
            adventurers.addMember(character);
        }
        
        System.out.println();
        
        // 2. Demonstrasi Singleton Pattern (Legendary Knight)
        System.out.println("2. LEGENDARY KNIGHT CREATION:");
        System.out.println("=============================");
        
        LegendaryKnight knight1 = LegendaryKnight.getInstance("Arthur");
        if (knight1 != null) {
            adventurers.addMember(knight1);
        }
        
        // Coba buat lagi - akan gagal
        LegendaryKnight knight2 = LegendaryKnight.getInstance("Lancelot");
        
        System.out.println();
        
        // 3. Menampilkan party
        System.out.println("3. PARTY OVERVIEW:");
        System.out.println("==================");
        adventurers.displayParty();
        System.out.println();
        
        // 4. Demonstrasi Polymorphism - Special abilities
        System.out.println("4. SPECIAL ABILITIES (Polymorphism):");
        System.out.println("====================================");
        
        for (Character character : adventurers.getMembers()) {
            character.useSpecialAbility(null);
            System.out.println();
        }
        
        // 5. Demonstrasi Interface - Skills
        System.out.println("5. SKILL USAGE (Interface Implementation):");
        System.out.println("==========================================");
        
        Character warrior = adventurers.getMember(0);
        Character mage = adventurers.getMember(1);
        Character archer = adventurers.getMember(2);
        
        // Warrior menggunakan skill
        System.out.println("--- Warrior Skills ---");
        warrior.useSkill(0, mage); // Power Strike pada mage
        
        System.out.println();
        
        // Mage menggunakan skill
        System.out.println("--- Mage Skills ---");
        mage.useSkill(1, mage); // Heal diri sendiri
        mage.useSkill(0, warrior); // Fireball pada warrior
        
        System.out.println();
        
        // Archer menggunakan skill
        System.out.println("--- Archer Skills ---");
        archer.useSkill(0, warrior); // Precision Shot
        
        System.out.println();
        
        // 6. Demonstrasi Basic Combat
        System.out.println("6. BASIC COMBAT:");
        System.out.println("================");
        
        System.out.println("Thorin attacks Gandalf:");
        warrior.basicAttack(mage);
        
        System.out.println("Gandalf retaliates:");
        mage.basicAttack(warrior);
        
        System.out.println();
        
        // 7. Level up demonstration
        System.out.println("7. LEVEL UP SYSTEM:");
        System.out.println("===================");
        
        warrior.gainExperience(150);
        mage.gainExperience(120);
        
        System.out.println();
        
        // 8. Final status
        System.out.println("8. FINAL PARTY STATUS:");
        System.out.println("======================");
        adventurers.displayAllStatus();
        
        // 9. Demonstrasi death dan reset Legendary Knight
        System.out.println("9. LEGENDARY KNIGHT MECHANICS:");
        System.out.println("==============================");
        
        if (knight1 != null) {
            System.out.println("Current Legendary Knight exists: " + LegendaryKnight.exists());
            
            // Simulate death
            knight1.takeDamage(knight1.getHealth());
            
            System.out.println("After death, can create new Legendary Knight: " + !LegendaryKnight.exists());
            
            // Now we can create a new one
            LegendaryKnight newKnight = LegendaryKnight.getInstance("Percival");
            if (newKnight != null) {
                System.out.println("New Legendary Knight created successfully");
            }
        }
        
        System.out.println();
        System.out.println("=== OOP CONCEPTS DEMONSTRATED ===");
        System.out.println("1. Abstract Class: Character with abstract methods");
        System.out.println("2. Inheritance: Warrior, Mage, Archer, LegendaryKnight extend Character");
        System.out.println("3. Interface: Skill interface implemented by various skill classes");
        System.out.println("4. Polymorphism: Same method calls, different implementations");
        System.out.println("5. Singleton Pattern: LegendaryKnight (exclusive role)");
        System.out.println("6. Encapsulation: Private fields with public methods");
        System.out.println("7. Method Overriding: Each class has unique implementations");
        System.out.println();
        System.out.println("Game system ready for expansion and gameplay!");
    }
}