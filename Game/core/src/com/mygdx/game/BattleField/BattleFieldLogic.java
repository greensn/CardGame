package com.mygdx.game.BattleField;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by 2weirdy on 2017-11-10.
 */

public class BattleFieldLogic {
    public final int width;
    public final int height;
    public final MinionNode[][] field;
    public boolean isLeftPlayerTurn = true;
    public boolean gameOver = false;

    public final ArrayList<MinionNode> leftPlayerMinions = new ArrayList<MinionNode>();
    public final ArrayList<MinionNode> rightPlayerMinions = new ArrayList<MinionNode>();

    public BattleFieldLogic(int width, int height){
        this.width = width;
        this.height = height;
        field = new MinionNode[width][height];
    }

    public MinionNode getMinionNode(int x, int y){
        if(width>x&&x>=0&&height>y&&y>=0){
            return field[x][y];
        }else{
            return null;
        }
    }

    public boolean addMinion(MinionNode m, int x, int y){
        if(width>x&&x>=0&&height>y&&y>=0) {
            if (field[x][y] == null) {
                field[x][y] = m;
                m.minion.xPos = x;
                m.minion.yPos = y;
                if (m.minion.isLeftPlayer) {
                    leftPlayerMinions.add(m);
                } else {
                    rightPlayerMinions.add(m);
                }
                return true;
            } else {
                return false;
            }
        }else{
            return false;
        }
    }

    public boolean doMovement(){
        ArrayList<MinionNode> curMinions = isLeftPlayerTurn ? leftPlayerMinions : rightPlayerMinions;
        final int xMod = isLeftPlayerTurn ? 1 : -1;
        for(MinionNode n:curMinions){
            Minion m = n.minion;
            if(field[m.xPos+xMod][m.yPos]==null){
                ++m.xPos;
                if(m.xPos == width-1){
                    gameOver = true;
                    return true;
                }
                field[m.xPos+xMod][m.yPos] = n;
                field[m.xPos][m.yPos] = null;
            }
        }
        Collections.sort(curMinions, new Comparator<MinionNode>() {
            @Override
            public int compare(MinionNode t0, MinionNode t1) {
                if(t0.minion.xPos<t1.minion.xPos){
                    return -xMod;
                }
                if(t0.minion.xPos>t1.minion.xPos){
                    return xMod;
                }
                return 0;
            }
        });
        return false;
    }

    public ArrayList<MinionNode> getInBoostRange(MinionNode n){
        Minion m = n.minion;
        ArrayList<MinionNode> buffTargets = new ArrayList<MinionNode>();
        MinionNode t;
        int range = n.minion.getAttribute("BuffRange");
        int xMod = n.minion.isLeftPlayer ? 1 : -1;
        switch (range) {
            case 3:
                t = getMinionNode(m.xPos - xMod, m.yPos - 1);
                if (t != null) {
                    buffTargets.add(t);
                }
                t = getMinionNode(m.xPos - xMod, m.yPos);
                if (t != null) {
                    buffTargets.add(t);
                }
                t = getMinionNode(m.xPos - xMod, m.yPos + 1);
                if (t != null) {
                    buffTargets.add(t);
                }
            case 2:
                t = getMinionNode(m.xPos + xMod, m.yPos - 1);
                if (t != null) {
                    buffTargets.add(t);
                }
                t = getMinionNode(m.xPos + xMod, m.yPos);
                if (t != null) {
                    buffTargets.add(t);
                }
                t = getMinionNode(m.xPos + xMod, m.yPos + 1);
                if (t != null) {
                    buffTargets.add(t);
                }
            case 1:
                t = getMinionNode(m.xPos, m.yPos - 1);
                if (t != null) {
                    buffTargets.add(t);
                }
                t = getMinionNode(m.xPos, m.yPos + 1);
                if (t != null) {
                    buffTargets.add(t);
                }
                break;
            case 0:
            default:
                return null;
        }
        return buffTargets;
    }

    public void doBuffs(){
        ArrayList<MinionNode> curMinions = isLeftPlayerTurn ? leftPlayerMinions : rightPlayerMinions;
        for (MinionNode n : curMinions) {
            n.minion.setAttribute("BuffedHealing",n.minion.getAttribute("Healing"));
            n.minion.setAttribute("BuffedAtk",n.minion.getAttribute("AttackDmg"));
            Minion m = n.minion;
            int healbuff = m.getAttribute("HealBuff");
            int atkbuff = m.getAttribute("AtkBuff");
            ArrayList<MinionNode> buffTargets = getInBoostRange(n);
            for (MinionNode target : buffTargets) {
                Minion targetm = target.minion;
                if(targetm.isLeftPlayer == m.isLeftPlayer) {
                    targetm.setAttribute("BuffedAtk", targetm.getAttribute("BuffedAtk") + atkbuff);
                    targetm.setAttribute("BuffedHealing", targetm.getAttribute("BuffedHealing") + healbuff);
                }
            }
        }
        for (MinionNode n : curMinions) {
            Minion m = n.minion;
            int healing = m.getAttribute("BuffedHealing");
            ArrayList<MinionNode> buffTargets = getInBoostRange(n);
            for (MinionNode target : buffTargets) {
                Minion targetm = target.minion;
                if(targetm.isLeftPlayer == m.isLeftPlayer) {
                    targetm.setAttribute("Health", targetm.getAttribute("Health") + healing);
                }
            }
        }
    }

    public void doAttacks(){
        ArrayList<MinionNode> curMinions = isLeftPlayerTurn ? leftPlayerMinions : rightPlayerMinions;
    }

    /**
     *
     * @return returns true if a player has won. Which player has won can be obtained from the field isLeftPlayerTurn.
     */
    public boolean doGameStep(){
        if(gameOver) return true;
        if(doMovement()) return true;

        doBuffs();
        doAttacks();

        return false;
    }

    public boolean addMinionAsTurn(MinionNode m, int x, int y, boolean isLeftPlayer){
        return false;
    }
}
