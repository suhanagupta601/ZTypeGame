import tester.*;    
import java.util.Random;          
import javalib.worldimages.*;  
import javalib.funworld.*;     
import java.awt.Color;

// represents an Active or Inactive Word
interface IWord {
  
  // draws the ZType game
  WorldScene drawWords(WorldScene acc);
  
  // continuously moves the active and inactive words throughout the game
  IWord move();
  
  // checks if first letter of word equals given string and consecutively removes letters from word
  IWord checkAndChangeWords(String letter);
  
  // checks if first letter of word equals given string and consecutively removes letters from word
  IWord checkAndChangeWordsNoActives(String letter);
  
  // checks if the IWord is an Active word
  boolean isActive();
  
  // checks if the IWord reached the bottom of the game
  boolean checkCoordinates();
 
  //check if the word is 6 letters and accesses the first letter of the word
  String getFirstLetter();
 
}

// represents a list of inactive or active words
interface ILoWord {
  
  // draws the list of words onto the worldscene
  WorldScene drawWords(WorldScene acc);
  
  // continuously moves the list of words throughout the game
  ILoWord move();
  
  // check if given string matches the first letter of the first word
  // in the list
  ILoWord checkAndChangeWords(String letter);
  
  //check if given string matches the first letter of the first word
  // in the list
  ILoWord checkAndChangeWordsNoActives(String letter);
  
  // adds IWords to the end of the list
  ILoWord addToEnd(IWord word);
 
  // counts how many active words are present
  int countActives();
 
  //checks and sees if any words have reached the bottom in the list
  boolean noWordsAtBottom();
   
  //checks if the first letter of this word is the same as the first substring of the word
  boolean allDiffStartLetters(String s);
}

class ZTypeWorld extends World {
  ILoWord words;
  Random rand;
  
  ZTypeWorld(ILoWord words) {
    this.words = words;
    this.rand =  new Random();
  }
  
  /*
   * fields:
   * - this.words...ILoWord
   * - this.rand... Random
   * 
   * methods:
   * makeScene()...WorldScene
   * makeFinalScene() - WorldScene
   * onTick() - World
   * onKeyEvent - World
   * WorldEnds()- WorldEnd
   * 
   * methods of fields:
   * drawWords(WordlsScene) - WorldScene
   * allDiffStartLetters(String s) -  boolean
   * this.words.addToEnd(IWord word)...ILoWord
   * this.words.move()... ILoWord 
   * this.words.countActives()... int
   * this.words.checkAndChangeWords(key)... ILoWord
   * this.words.checkAndChangeWordsNoActives(key)...ILoWord
   * 
   */
  
  
  ZTypeWorld(ILoWord words, Random rand) { 
    this.words = words;
    this.rand =  rand;
  }
  
  //draws words onto the WorldScene
  public WorldScene makeScene() {
    return this.words.drawWords(new WorldScene(400, 600));
  }
    
  // tells player they  lost game and ends game
  public WorldScene makeFinalScene() {
    TextImage t = new TextImage("you lost", Color.BLACK);
    return (new WorldScene(400, 600).placeImageXY(t, 100, 100));
  }
    
  // words move and are produced every tick that has passed
  public World onTick() {
    Utils u = new Utils();
    Random random = new Random();
    int randomToNumber = random.nextInt(360) + 20;
    
    //make words where there is only 1 active word
    String newWord = u.produceWord();

    if (this.words.allDiffStartLetters(newWord)) {
      ILoWord newWords = this.words.addToEnd(new InactiveWord(newWord, randomToNumber, 0));
      return new ZTypeWorld(newWords.move());
  
    }
     
    else {
      return new ZTypeWorld(this.words.move());
    }
   
  }
  
  // determines a key event by checking if there is an active word
  public World onKeyEvent(String key) {
    
    if (this.words.countActives() == 1) {
      //if there is an active word
      return new ZTypeWorld(this.words.checkAndChangeWords(key)); 
    }
    //if there is no active word
    else {
      return new ZTypeWorld(this.words.checkAndChangeWordsNoActives(key)); 
    } 
  }
  
  // determines the end of the game
  public WorldEnd worldEnds() {
    if (!(this.words.noWordsAtBottom())) {
      return new WorldEnd(true, this.makeFinalScene());
    }
    else {
      return new WorldEnd(false, this.makeScene());
    }
  }
}

//represents an empty list of ILoWords
class MtLoWord implements ILoWord {
  
  /*
  * fields:
  *
  * methods:
  * - draw(WorldScene acc) ...WorldScene
  * - move() ... ILoWord
  * - checkAndChangeWords(String letter)... ILoWord
  * - checkAndChangeWordsNoActives(String letter)...ILoWord
  * - addToEnd(IWord word)... ILoWord
  * - countActives()... int
  * - noWordsAtBottom()... boolean
  *
  * methods of fields:
  *
  */
  
  // draws this empty list of IWords onto the WorldScene
  public WorldScene drawWords(WorldScene acc) {
    return acc;
  }
  
  // moves this empty list of IWords from top to bottom of the WorldScene
  public ILoWord move() {
    return this;
  }
  
  // checks if first letter types is equals to first letter of the first word
  // in this empty list
  public ILoWord checkAndChangeWords(String letter) {
    return this;
  }
  
  //checks if first letter types is equals to first letter of the first word
  // in this empty list
  public ILoWord checkAndChangeWordsNoActives(String letter) {
    return this;
  }
  
  // adds given words to the end of this empty list
  public ILoWord addToEnd(IWord word) {
    return new ConsLoWord(word, this);
  }
  
  // counts how many active words are in the non-empty list
  public int countActives() {
    return 0;
  }
  
  //checks if t ehre are no words at bottom of this empty list
  public boolean noWordsAtBottom() {
    return true;
  }
  
  // checks to make sure if words that start with teh same letter are removed one at a time
  public boolean allDiffStartLetters(String s) {
    return true;
  }
}

// represents a non-empty list of ILoWords
class ConsLoWord implements ILoWord {
  IWord first;
  ILoWord rest;
 
  ConsLoWord(IWord first, ILoWord rest) {
    this.first = first;
    this.rest = rest;
      
  }
  
  /*
  * fields:
  * - this.first ... IWord
  * - this.rest ... ILoWord
  *
  * methods:
  * - draw(WorldScene acc) ...WorldScene
  * - move() ... ILoWord
  * - checkAndChangeWords(String letter)... ILoWord
  * - checkAndChangeWordsNoActives(String letter)...ILoWord
  * - addToEnd(IWord word)... ILoWord
  * - countActives()... int
  * - noWordsAtBottom()... boolean
  *
  * methods of fields:
  * - checkCoordinates()...boolean
  * - getFirstLetter()... String
  * - noWordsAtBottom()...boolean
  *
  */
  
  // draws the list of IWords onto the WorldScene in this non-empty list
  public WorldScene drawWords(WorldScene acc) {
    return this.rest.drawWords(this.first.drawWords(acc));
  }
  
  // moves this non-empty list of IWords from top to bottom of the WorldScene
  public ILoWord move() {
    return new ConsLoWord(this.first.move(), this.rest.move());
  }
 
  //checks if first letter types is equals to first letter of the first word
  // in this empty list
  public ILoWord checkAndChangeWords(String letter) {
    return new ConsLoWord(this.first.checkAndChangeWords(letter),
       this.rest.checkAndChangeWords(letter));
  }
  
  //checks if first letter types is equals to first letter of the first word
  // in this empty list
  public ILoWord checkAndChangeWordsNoActives(String letter) {
    return new ConsLoWord(this.first.checkAndChangeWordsNoActives(letter),
       this.rest.checkAndChangeWordsNoActives(letter));
  }
  
  // adds given words to the end of this non-empty list
  public ILoWord addToEnd(IWord word) {
    return new ConsLoWord(this.first, this.rest.addToEnd(word));
  }
 
  // counts how many active words are in this non-empty list
  public int countActives() {
    if (this.first.isActive()) {
      return 1 + this.rest.countActives();
    }
  
    else {
      return this.rest.countActives();
    }
  }
 
  //returns false if any words are at the bottom
  //returns true if there aren't any words at the bottom
  public boolean noWordsAtBottom() {
    if (this.first.checkCoordinates()) {
      return false;
    }
    else {
      return this.rest.noWordsAtBottom();
    }
  }
 
  // checks to make sure if words that start with teh same letter are removed one at a time
  public boolean allDiffStartLetters(String s) {
    if (this.first.getFirstLetter().equals(s.substring(0, 1))) {
      return false;
    }
    else {
      return this.rest.allDiffStartLetters(s);
    }
  }
}

// represents an active word once the correct letter is typed
class ActiveWord implements IWord {
  String word;
  int x;
  int y;
 
  ActiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
  
  }
  
  /*
  * fields:
  * - this.word...String
  * - this.x... int
  * - this.y...int
  *
  * methods:
  * - draw(WorldScene acc) ...WorldScene
  * - move() ... ILoWord
  * - checkAndChangeWords(String letter)... IWord
  * - checkAndChangeWordsNoActives(String letter)...IWord
  * - isActive()... boolean
  * - getFirstLetter()... String
  * - checkCoordinates()... boolean
  *
  * methods of fields:
  *
  */
  
  // draws words onto the worldscene
  public WorldScene drawWords(WorldScene acc) {
    TextImage ti = new TextImage(this.word, Color.BLUE);
    return acc.placeImageXY(ti, this.x, this.y);
  }
  
  // moves the word across to the bottom of teh game
  public IWord move() {
    return new ActiveWord(this.word, this.x, this.y + 40);
  }
  
  //activeWord to inActive
  public IWord checkAndChangeWords(String letter) {
    if (this.word.length() > 0 && this.word.substring(0, 1).equals(letter)) {
      return new ActiveWord(this.word.substring(1), this.x, this.y);
    }
    
    else if (this.word.length() == 0) {
      return new InactiveWord("", this.x, this.y);
    }
    
    //if the length is greater than 0 and they type the wrong letter
    else {
      return this;
    }
  }
  
  //checks if first letter types is equals to first letter of the first word
  // in this empty list
  public IWord checkAndChangeWordsNoActives(String letter) {
    return new InactiveWord(this.word, this.x, this.y);
  }
  
  // is word active 
  public boolean isActive() {
    return true;
  }
  
  //access first letter of teh word
  public String getFirstLetter() {
    if (this.word.length() == 6) {
      return this.word.substring(0, 1);
    }
    
    else {
      return "";
    }
  
  }
  
  // has word reached bottom
  public boolean checkCoordinates() {
    return this.y >= 600;
  }
 
}

//represents an inactive word
class InactiveWord implements IWord {
  String word;
  int x;
  int y;
 
  InactiveWord(String word, int x, int y) {
    this.word = word;
    this.x = x;
    this.y = y;
 
  }
  /*
  * fields:
  * - this.word...String
  * - this.x... int
  * - this.y...int
  *
  * methods:
  * - draw(WorldScene acc) ...WorldScene
  * - move() ... ILoWord
  * - checkAndChangeWords(String letter)... IWord
  * - checkAndChangeWordsNoActives(String letter)...IWord
  * - isActive()... boolean
  * - getFirstLetter()... String
  * - checkCoordinates()... boolean
  *
  * methods of fields:
  *
  */
  
  // draws word onto worldscene
  public WorldScene drawWords(WorldScene acc) {
    TextImage ti = new TextImage(this.word, Color.BLUE);
    return acc.placeImageXY(ti, this.x, this.y);
  }
  
  //moves teh word down
  public IWord move() {
    return new InactiveWord(this.word, this.x, this.y + 40);
  }
  
  //if the user types letter that matches this inactive word, word should become active
  public IWord checkAndChangeWords(String letter) {
    return this;
  }
  
  //checks if first letter types is equals to first letter of the first word
  // in this empty list
  public IWord checkAndChangeWordsNoActives(String letter) {
    if (this.word.length() == 6 && this.word.substring(0, 1).equals(letter)) {
      return new ActiveWord(this.word.substring(1), this.x, this.y);
    }
    
    else {
      return this;
    }
    
  }
 
  //is word active
  public boolean isActive() {
    return false;
  }
 
  // access first letter of word
  public String getFirstLetter() {
    if (this.word.length() == 6) {
      return this.word.substring(0, 1);
    }
    
    else {
      return "";
    }
  }
  
  //are the coordinates more than 60o or reached bottom
  public boolean checkCoordinates() {
    return this.y >= 600;
  } 
 
}

class Utils {
  String produceLetter() {
    String alphabet = "abcdefghijklmnopqrstuvwxyz";
    Random random = new Random();
    int randomToNumber = random.nextInt(26);
    return alphabet.substring(randomToNumber, randomToNumber + 1);
  }
 
  //produces a word with random 6 letters
  String produceWord() {
    return  produceLetter() + produceLetter() + produceLetter()
      + produceLetter() + produceLetter() + produceLetter();
  }
}



class ExamplesAnimation {
  
  ILoWord mt = new MtLoWord();
  Utils u = new Utils();
  IWord word1 = new ActiveWord(u.produceWord(), 100, 10);
  IWord word2 = new InactiveWord(u.produceWord(), 150, 10);
  ILoWord list1 = new ConsLoWord(word1, mt);
  ILoWord list2 = new ConsLoWord(word2, list1);
 
    
  boolean testBigBang(Tester t) {
    ZTypeWorld world = new ZTypeWorld(this.list1);
    int worldWidth = 400;
    int worldHeight = 600;
    double tickRate = 2;
    return world.bigBang(worldWidth, worldHeight, tickRate);
  }
 
  IWord word3 = new InactiveWord("helloa", 100, 10);
  IWord word4 = new InactiveWord("goodby", 150, 10);
  IWord word5 = new InactiveWord("oodbye", 150, 10);
  IWord word5Active = new ActiveWord("oodbye", 150, 10);
  ILoWord list3 = new ConsLoWord(word3, mt);
  ILoWord list4 = new ConsLoWord(word4, list3); 
   
  //changed list5 to use word5Active instead of word5
  ILoWord list5 = new ConsLoWord(word5Active, list3);
  ILoWord list6 = new ConsLoWord(this.word3, new ConsLoWord(this.word1, this.mt));
  IWord word1Coor = new ActiveWord(u.produceWord(), 100, 10);
  IWord word2Coor = new ActiveWord(u.produceWord(), 100, 600);
  IWord word3Coor = new InactiveWord("helloa", 100, 600);
  IWord word4Coor = new InactiveWord("goodby", 150, 10);
  ILoWord list7 = new ConsLoWord(this.word3Coor, this.mt);
  ZTypeWorld w = new ZTypeWorld(list4, new Random(7));
  ZTypeWorld w2 = new ZTypeWorld(list5, new Random(7));
  ZTypeWorld emptyListWorld = new ZTypeWorld(mt, new Random(7));
  
 
  boolean testOnKeyEvent(Tester t) {
    return
       t.checkExpect(this.w.onKeyEvent("g"), w2);
  }
 
 
  boolean testMove(Tester t) {
    return
       t.checkExpect(this.word3.move(), new InactiveWord("helloa", 100, 50))
       && t.checkExpect(this.word4.move(), new InactiveWord("goodby", 150, 50))
       && t.checkExpect(this.mt.move(), this.mt)
       && t.checkExpect(this.list3.move(), 
           new ConsLoWord(new InactiveWord("helloa", 100, 50), this.mt))
       && t.checkExpect(this.list4.move(), new ConsLoWord(new InactiveWord("goodby", 150, 50),
           new ConsLoWord(new InactiveWord("helloa", 100, 50), this.mt)));
  }
  
  
  boolean testCheckAndChangeWords(Tester t) {
    return
       // testing from active class
       //t.checkExpect(this.word3.checkAndChangeWords("h"), new ActiveWord("elloa", 100, 10))
       t.checkExpect(this.word4.checkAndChangeWords("g"), this.word4)
       && t.checkExpect(this.word5.checkAndChangeWords("g"), this.word5);
  }
  
  
  void testCheckAndChangeWordsNoActives(Tester t) {
    t.checkExpect(this.word3.checkAndChangeWordsNoActives("h"),
        new ActiveWord("elloa", 100, 10));  
    t.checkExpect(this.word3.checkAndChangeWordsNoActives("z"), this.word3);
    t.checkExpect(this.word5Active.checkAndChangeWordsNoActives("o"),
        new InactiveWord("oodbye", 150, 10));
   
    //t.checkExpect(this.mt.checkAndChangeWordsNoActives("f"), this.mt);
    t.checkExpect(this.list3.checkAndChangeWordsNoActives("h"), 
        new ConsLoWord(new ActiveWord("elloa", 100, 10), this.mt));
    t.checkExpect(this.list3.checkAndChangeWordsNoActives("s"), this.list3);
       
  }
  
  //  tests for onTick
  boolean testOnTick(Tester t) {
    return
       t.checkExpect(this.w.onTick(), new ZTypeWorld(
           new ConsLoWord(new InactiveWord("goodby", 150, 50),
           new ConsLoWord(new InactiveWord("helloa", 100, 50), this.mt)), new Random(7)));
  }
  

  
 
  TextImage ti1 = new TextImage("goodby", Color.BLUE);
  TextImage ti2 = new TextImage("helloa", Color.BLUE);
  TextImage ti3 = new TextImage("oodbye", Color.BLUE);
  
  WorldScene mtWorldScene = new WorldScene(400, 600);
  WorldScene oneWordWorld = mtWorldScene.placeImageXY(ti1, 150, 10);
  WorldScene otherOneWordWorld = mtWorldScene.placeImageXY(ti2, 100, 10);
  WorldScene finalOneWordWorld = mtWorldScene.placeImageXY(ti3, 150, 10);
  WorldScene twoWordWorld = oneWordWorld.placeImageXY(ti2, 100, 10);
   
 
  //tests for makeScene
  boolean testMakeScene(Tester t) {
    return
       t.checkExpect(this.emptyListWorld.makeScene(), new WorldScene(400, 600))
       && t.checkExpect(this.w.makeScene(), twoWordWorld);
  }
 
 
  //tests for draw method in IWord
  boolean testDrawIWord(Tester t) {
    return
       t.checkExpect(this.word4.drawWords(this.mtWorldScene), this.oneWordWorld)
       && t.checkExpect(this.word4.drawWords(this.otherOneWordWorld), this.twoWordWorld);
      
  }
 
 
  ILoWord onlyWord4list = new ConsLoWord(word4, mt); 
  ILoWord onlyWord3list = new ConsLoWord(word3, mt); 
  ILoWord listWord3AndWord4 = new ConsLoWord(word3, onlyWord4list);
  
  WorldScene oneWordWorld2 = mtWorldScene.placeImageXY(ti1, 150, 10);
  WorldScene twoWordWorld2 = oneWordWorld2.placeImageXY(ti2, 100, 10);
  WorldScene threeWordWorld2 = twoWordWorld2.placeImageXY(ti3, 150, 10);
  
  
  void testCountActives(Tester t) {
    t.checkExpect(this.mt.countActives(), 0);
    t.checkExpect(this.list5.countActives(), 1);
    t.checkExpect(this.list4.countActives(), 0);
  }
  
  void testNoWordsAtBottom(Tester t) {
    t.checkExpect(this.mt.noWordsAtBottom(), true);
    t.checkExpect(this.list4.noWordsAtBottom(), true);
    t.checkExpect(this.list7.noWordsAtBottom(), false);
  }
   
  //tests for draw method in ILoWord
  boolean testDrawILoWord(Tester t) {
    return
        t.checkExpect(this.onlyWord4list.drawWords(this.mtWorldScene), this.oneWordWorld)
        && t.checkExpect(this.onlyWord3list.drawWords(this.oneWordWorld), this.twoWordWorld)
        && t.checkExpect(this.listWord3AndWord4.drawWords(this.finalOneWordWorld), 
            this.threeWordWorld2); 
  }
  
  IWord mtActiveStr = new ActiveWord("", 100, 500);
   
  // tests if first letter is taken
  void testGetFirstLetter(Tester t) {
    
    // cannot have empty inactive word since inactive word always contains 6 letters
    t.checkExpect(this.word3.getFirstLetter(), "h");
    t.checkExpect(this.word5Active.getFirstLetter(), "o");
    t.checkExpect(this.mtActiveStr, this.mtActiveStr);     
  }
  
  
  
  void testCheckCoordinates(Tester t) {
    t.checkExpect(this.word3Coor.checkCoordinates(), true);
    t.checkExpect(this.word4Coor.checkCoordinates(), false);
    t.checkExpect(this.word1Coor.checkCoordinates(), false);
    t.checkExpect(this.word2Coor.checkCoordinates(), true);
  
  }
  
  ILoWord list1real = new ConsLoWord(word3, mt);
  ILoWord list2real = new ConsLoWord(word4, list1real);
  ILoWord list3real = new ConsLoWord(word5, list2real);
  
  boolean testAllDiffStartLetters(Tester t) {
    return
        t.checkExpect(this.list3real.allDiffStartLetters("zelloo"), true)
        && t.checkExpect(this.list3real.allDiffStartLetters("helloo"), false); 
  }
  
  void testAddToEnd(Tester t) {
    t.checkExpect(this.list3.addToEnd(this.word3), 
        new ConsLoWord(this.word3, new ConsLoWord(this.word3, this.mt)));
    t.checkExpect(this.mt.addToEnd(this.word3), this.list3);
    t.checkExpect(this.list4.addToEnd(this.word5Active), 
        new ConsLoWord(new InactiveWord("goodby", 150, 10),
        new ConsLoWord(new InactiveWord("helloa", 100, 10), 
            new ConsLoWord(this.word5Active, this.mt))));
  }
  
}
