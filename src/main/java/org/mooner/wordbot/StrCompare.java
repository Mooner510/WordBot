package org.mooner.wordbot;

import org.mooner.wordbot.game.StringManager;
import org.mooner.wordbot.utils.BotUtils;

import java.util.*;

public class StrCompare {
    public static final String ENG_KEY = "rRseEfaqQtTdwWczxvgkoiOjpuPhynbml";
    public static final String KOR_KEY = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎㅏㅐㅑㅒㅓㅔㅕㅖㅗㅛㅜㅠㅡㅣ";
    public static final String CHO_DATA = "ㄱㄲㄴㄷㄸㄹㅁㅂㅃㅅㅆㅇㅈㅉㅊㅋㅌㅍㅎ";
    public static final String JUNG_DATA = "ㅏㅐㅑㅒㅓㅔㅕㅖㅗㅘㅙㅚㅛㅜㅝㅞㅟㅠㅡㅢㅣ";
    public static final String JONG_DATA = "ㄱㄲㄳㄴㄵㄶㄷㄹㄺㄻㄼㄽㄾㄿㅀㅁㅂㅄㅅㅆㅇㅈㅊㅋㅌㅍㅎ";

    public static String engTypeToKor(String src) {
        StringBuilder res = new StringBuilder();
        if (src.isEmpty()) return res.toString();

        int nCho = -1, nJung = -1, nJong = -1;

        for (char ch : src.toCharArray()) {
            int p = ENG_KEY.indexOf(ch);
            if (p == -1) {
                if (nCho != -1) {
                    if (nJung != -1) res.append(makeHangul(nCho, nJung, nJong));
                    else res.append(CHO_DATA.charAt(nCho));
                } else {
                    if (nJung != -1) res.append(JUNG_DATA.charAt(nJung));
                    else if (nJong != -1) res.append(JONG_DATA.charAt(nJong));
                }
                nCho = -1;
                nJung = -1;
                nJong = -1;
                res.append(ch);
            } else if (p < 19) {
                if (nJung != -1) {
                    if (nCho == -1) {
                        res.append(JUNG_DATA.charAt(nJung));
                        nJung = -1;
                        nCho = CHO_DATA.indexOf(KOR_KEY.charAt(p));
                    } else {
                        if (nJong == -1) {
                            nJong = JONG_DATA.indexOf(KOR_KEY.charAt(p));
                            if (nJong == -1) {
                                res.append(makeHangul(nCho, nJung, nJong));
                                nCho = CHO_DATA.indexOf(KOR_KEY.charAt(p));
                                nJung = -1;
                            }
                        } else if (nJong == 0 && p == 9) nJong = 2;
                        else if (nJong == 3 && p == 12) nJong = 4;
                        else if (nJong == 3 && p == 18) nJong = 5;
                        else if (nJong == 7 && p == 0) nJong = 8;
                        else if (nJong == 7 && p == 6) nJong = 9;
                        else if (nJong == 7 && p == 7) nJong = 10;
                        else if (nJong == 7 && p == 9) nJong = 11;
                        else if (nJong == 7 && p == 16) nJong = 12;
                        else if (nJong == 7 && p == 17) nJong = 13;
                        else if (nJong == 7 && p == 18) nJong = 14;
                        else if (nJong == 16 && p == 9) nJong = 17;
                        else {
                            res.append(makeHangul(nCho, nJung, nJong));
                            nCho = CHO_DATA.indexOf(KOR_KEY.charAt(p));
                            nJung = -1;
                            nJong = -1;
                        }
                    }
                } else {
                    if (nCho == -1) {
                        if (nJong != -1) {
                            res.append(JONG_DATA.charAt(nJong));
                            nJong = -1;
                        }
                        nCho = CHO_DATA.indexOf(KOR_KEY.charAt(p));
                    } else if (nCho == 0 && p == 9) {
                        nCho = -1;
                        nJong = 2;
                    } else if (nCho == 2 && p == 12) {
                        nCho = -1;
                        nJong = 4;
                    } else if (nCho == 2 && p == 18) {
                        nCho = -1;
                        nJong = 5;
                    } else if (nCho == 5 && p == 0) {
                        nCho = -1;
                        nJong = 8;
                    } else if (nCho == 5 && p == 6) {
                        nCho = -1;
                        nJong = 9;
                    } else if (nCho == 5 && p == 7) {
                        nCho = -1;
                        nJong = 10;
                    } else if (nCho == 5 && p == 9) {
                        nCho = -1;
                        nJong = 11;
                    } else if (nCho == 5 && p == 16) {
                        nCho = -1;
                        nJong = 12;
                    } else if (nCho == 5 && p == 17) {
                        nCho = -1;
                        nJong = 13;
                    } else if (nCho == 5 && p == 18) {
                        nCho = -1;
                        nJong = 14;
                    } else if (nCho == 7 && p == 9) {
                        nCho = -1;
                        nJong = 17;
                    } else {
                        res.append(CHO_DATA.charAt(nCho));
                        nCho = CHO_DATA.indexOf(KOR_KEY.charAt(p));
                    }
                }
            } else {
                if (nJong != -1) {
                    int newCho;
                    if (nJong == 2) {
                        nJong = 0;
                        newCho = 9;
                    } else if (nJong == 4) {
                        nJong = 3;
                        newCho = 12;
                    } else if (nJong == 5) {
                        nJong = 3;
                        newCho = 18;
                    } else if (nJong == 8) {
                        nJong = 7;
                        newCho = 0;
                    } else if (nJong == 9) {
                        nJong = 7;
                        newCho = 6;
                    } else if (nJong == 10) {
                        nJong = 7;
                        newCho = 7;
                    } else if (nJong == 11) {
                        nJong = 7;
                        newCho = 9;
                    } else if (nJong == 12) {
                        nJong = 7;
                        newCho = 16;
                    } else if (nJong == 13) {
                        nJong = 7;
                        newCho = 17;
                    } else if (nJong == 14) {
                        nJong = 7;
                        newCho = 18;
                    } else if (nJong == 17) {
                        nJong = 16;
                        newCho = 9;
                    } else {
                        newCho = CHO_DATA.indexOf(JONG_DATA.charAt(nJong));
                        nJong = -1;
                    }
                    if (nCho != -1) res.append(makeHangul(nCho, nJung, nJong));
                    else res.append(JONG_DATA.charAt(nJong));

                    nCho = newCho;
                    nJung = -1;
                    nJong = -1;
                }
                if (nJung == -1) nJung = JUNG_DATA.indexOf(KOR_KEY.charAt(p));
                else if (nJung == 8 && p == 19) nJung = 9;
                else if (nJung == 8 && p == 20) nJung = 10;
                else if (nJung == 8 && p == 32) nJung = 11;
                else if (nJung == 13 && p == 23) nJung = 14;
                else if (nJung == 13 && p == 24) nJung = 15;
                else if (nJung == 13 && p == 32) nJung = 16;
                else if (nJung == 18 && p == 32) nJung = 19;
                else {
                    if (nCho != -1) {
                        res.append(makeHangul(nCho, nJung, nJong));
                        nCho = -1;
                    } else res.append(JUNG_DATA.charAt(nJung));
                    nJung = -1;
                    res.append(KOR_KEY.charAt(p));
                }
            }
        }

        // 마지막 한글이 있으면 처리
        if (nCho != -1) {
            if (nJung != -1)			// 초성+중성+(종성)
                res.append(makeHangul(nCho, nJung, nJong));
            else                		// 초성만
                res.append(CHO_DATA.charAt(nCho));
        } else {
            if (nJung != -1)			// 중성만
                res.append(JUNG_DATA.charAt(nJung));
            else {						// 복자음
                if (nJong != -1)
                    res.append(JONG_DATA.charAt(nJong));
            }
        }

        return res.toString();
    }

    private static String makeHangul(int nCho, int nJung, int nJong) {
        return Character.toString(0xac00 + nCho * 21 * 28 + nJung * 28 + nJong + 1);
    }

    public static String korTypeToEng(String src) {
        StringBuilder res = new StringBuilder();
        if (src.isEmpty()) return res.toString();

        for (char ch : src.toCharArray()) {
            int nCode = ch;
            final int nCho = CHO_DATA.indexOf(ch), nJung = JUNG_DATA.indexOf(ch), nJong = JONG_DATA.indexOf(ch);
            int[] arrKeyIndex = {-1, -1, -1, -1, -1};

            if (0xac00 <= nCode && nCode <= 0xd7a3) {
                nCode -= 0xac00;
                arrKeyIndex[0] = nCode / (21 * 28);
                arrKeyIndex[1] = (nCode / 28) % 21;
                arrKeyIndex[3] = nCode % 28 - 1;
            } else if (nCho != -1) arrKeyIndex[0] = nCho;
            else if (nJung != -1) arrKeyIndex[1] = nJung;
            else if (nJong != -1) arrKeyIndex[3] = nJong;
            else res.append(ch);

            if (arrKeyIndex[1] != -1) {
                if (arrKeyIndex[1] == 9) {
                    arrKeyIndex[1] = 27;
                    arrKeyIndex[2] = 19;
                } else if (arrKeyIndex[1] == 10) {
                    arrKeyIndex[1] = 27;
                    arrKeyIndex[2] = 20;
                } else if (arrKeyIndex[1] == 11) {
                    arrKeyIndex[1] = 27;
                    arrKeyIndex[2] = 32;
                } else if (arrKeyIndex[1] == 14) {
                    arrKeyIndex[1] = 29;
                    arrKeyIndex[2] = 23;
                } else if (arrKeyIndex[1] == 15) {
                    arrKeyIndex[1] = 29;
                    arrKeyIndex[2] = 24;
                } else if (arrKeyIndex[1] == 16) {
                    arrKeyIndex[1] = 29;
                    arrKeyIndex[2] = 32;
                } else if (arrKeyIndex[1] == 19) {
                    arrKeyIndex[1] = 31;
                    arrKeyIndex[2] = 32;
                } else {
                    arrKeyIndex[1] = KOR_KEY.indexOf(JUNG_DATA.charAt(arrKeyIndex[1]));
                }
            }
            if (arrKeyIndex[3] != -1) {
                if (arrKeyIndex[3] == 2) {
                    arrKeyIndex[3] = 0;
                    arrKeyIndex[4] = 9;
                } else if (arrKeyIndex[3] == 4) {
                    arrKeyIndex[3] = 2;
                    arrKeyIndex[4] = 12;
                } else if (arrKeyIndex[3] == 5) {
                    arrKeyIndex[3] = 2;
                    arrKeyIndex[4] = 18;
                } else if (arrKeyIndex[3] == 8) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 0;
                } else if (arrKeyIndex[3] == 9) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 6;
                } else if (arrKeyIndex[3] == 10) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 7;
                } else if (arrKeyIndex[3] == 11) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 9;
                } else if (arrKeyIndex[3] == 12) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 16;
                } else if (arrKeyIndex[3] == 13) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 17;
                } else if (arrKeyIndex[3] == 14) {
                    arrKeyIndex[3] = 5;
                    arrKeyIndex[4] = 18;
                } else if (arrKeyIndex[3] == 17) {
                    arrKeyIndex[3] = 7;
                    arrKeyIndex[4] = 9;
                } else {
                    arrKeyIndex[3] = KOR_KEY.indexOf(JONG_DATA.charAt(arrKeyIndex[3]));
                }
            }
            for (int j = 0; j < 5; j++) if (arrKeyIndex[j] != -1) res.append(ENG_KEY.charAt(arrKeyIndex[j]));
        }

        return res.toString();
    }

    public static void main(String[] args) {
        List<String> list = List.of("문성화", "최승우", "김현석", "양지은", "양운석", "김승우", "문승우", "삼각김밥", "돼지양고기", "김치찌개", "볶은김치볶음밥", "김승화", "최현석", "현석최", "김성화", "김지은", "최지은", "최운석", "심각한 김");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String s = scanner.nextLine();
            System.out.println(list.stream()
                    .sequential()
                    .map(v -> new AbstractMap.SimpleImmutableEntry<>(v, StringManager.findSimilarity(s, v)))
                    .filter(v -> v.getValue() > 0)
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .toList());
        }
    }
}
