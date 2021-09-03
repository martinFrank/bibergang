package com.github.martinfrank.bibergang;

public class BibergangCardColumn {

    public BibergangCard topCard;
    public BibergangCard bottomCard;
    private final String topCardId;
    private final String bottomCardId;

    public BibergangCardColumn(int columnIndex) {
        topCardId = BibergangCard.mapIndex(columnIndex*2);
        bottomCardId = BibergangCard.mapIndex(columnIndex*2+1);
    }

    public BibergangCard getCard(String id) {
        if(topCardId.equalsIgnoreCase(id)){
            return topCard;
        }
        if(bottomCardId.equalsIgnoreCase(id)){
            return bottomCard;
        }
        return null;
    }

    public void setCard(BibergangCard card, String id) {
        if(topCardId.equalsIgnoreCase(id)){
            topCard = card;
        }
        if(bottomCardId.equalsIgnoreCase(id)){
            bottomCard = card;
        }
    }

    public BibergangCard getPairCard(String id) {
        if(topCardId.equalsIgnoreCase(id)){
            return bottomCard;
        }
        if(bottomCardId.equalsIgnoreCase(id)){
            return topCard;
        }
        return null;
    }

    public boolean isPair() {
        if (topCard.isBiber()){
            return true;
        }
        if(bottomCard.isBiber()){
            return true;
        }
        return topCard.getValue() == bottomCard.getValue();
    }

    public boolean isVisiblePair() {
        if(topCard.isRevealed() && bottomCard.isRevealed() ){
            if (topCard.isBiber() || bottomCard.isBiber() ){
                return true;
            }
            return topCard.getValue() == bottomCard.getValue();
        }
        return false;
    }

    public String getTopCardId(){
        return topCardId;
    }

    public String getBottomCardId(){
        return bottomCardId;
    }
}
