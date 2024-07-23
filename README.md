# LeafDoc
이 프로젝트는 장미나무와 고무나무 잎의 병충해를 진단하는 웹 서버를 구현하였습니다/ 딥러닝 기술을 통해 정확하고 신속한 병충해 진단을 제공하여 식물 건강 관리를 지원합니다.

## 팀멤버
[이 현송](http://github.com/gfg), [조 민재](https://github.com/clamix)

## 모티브
세계 인구가 급속도로 증가하고 있으며 이를 지원하기 위해 충분한 식량 자원이 필요합니다. 그러나 기후 변화, 오염 및 식물 질병과 같은 식량 생산의 심각한 문제들이 발생하고 있습니다. 우리는 스마트폰을 유용하게 사용하여 식물 질병을 즉각적으로 감지하고 치료 방법을 제시해 질병을 치료하고 퍼지는 것을 방지할 수 있다고 생각하여 인류에 이바지 할 수 있다고 생각하였습니다. 

위의 아이디어를 구현하기 위해 유사 프로젝트를 찾던중 
Greendoc(https://github.com/donmccurdy/greendoc) 프로젝트를 clone하여 개발을 시작하였습니다.

## 문제점
1. 병충해 걸린 잎을 구하기가 어려워 데이터가 너무 적게 수집됨
2. Greendoc은 예전 android 버전으로 개발되어 최신 개발환경에서 build가 불가능함

## 해결방법
1. 데이터 부족을 극복하기 위해 Plantvillege 데이터 셋으로 학습된 Sqeezenet을 하여 transfer learning을 함
2. Take-Picture(https://github.com/Mr-Sepehr-sa/Take-Picture) 을 참고하여 Android app을 다시 개발함

## reference
[1] “[1602.07360] SqueezeNet: AlexNet-level accuracy with 50x fewer parameters and <0.5MB model size”.  [Online]. Available at: https://arxiv.org/abs/1602.07360



