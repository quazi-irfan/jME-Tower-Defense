package TowerDefense;

/**
 *
 * Instruction from the book (Page 84) : Create a  Charges class. The charges that the towers shoot are instances of a plain
 * old Java object that stores a damage value and the number of remaining bullets.
 * (You can extend this class and add accessors that support different types of attacks,
 * such as freeze or blast damage, or different forces.)
 * 
 */
public class Charge {
   private int damageValue;
   private int bulletCount; 

    public Charge(int damageValue, int bulletCount) {
        setBulletCount(bulletCount);
        setDamageValue(damageValue);
    }

    public Charge() {
        setBulletCount(2);
        setDamageValue(5);
    }

    public int getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    public int getBulletCount() {
        return bulletCount;
    }

    public void setBulletCount(int bulletCount) {
        this.bulletCount = bulletCount;
    }
   
    public void fireBullet()   {
        this.bulletCount = this.bulletCount - 1;
    }
}
