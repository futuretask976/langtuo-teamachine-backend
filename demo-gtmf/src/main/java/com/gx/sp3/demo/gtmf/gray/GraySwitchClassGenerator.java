package com.gx.sp3.demo.gtmf.gray;//package com.gx.gtmf.framework.gray;
//
//import com.gx.gtmf.framework.core.BusinessTemplate;
//import com.gx.gtmf.framework.metadata.BusinessDefMetadata;
//import com.gx.gtmf.framework.scan.ScanClassUtils;
//import com.gx.gtmf.framework.scan.ScanHandler;
//import org.springframework.asm.*;
//import org.springframework.core.io.Resource;
//import org.springframework.core.type.classreading.MetadataReader;
//import org.springframework.core.type.classreading.MetadataReaderFactory;
//
///**
// * @author miya
// */
//class DefineSwitchClassloader extends ClassLoader {
//    public DefineSwitchClassloader(ClassLoader parent) {
//        super(parent);
//    }
//
//    public Class<?> defineClass(String className, byte[] classByte) {
//        return defineClass(className, classByte,0, classByte.length);
//    }
//}
//
///**
// * @author miya
// */
//public class GraySwitchClassGenerator {
//    /**
//     *
//     */
//    private static DefineSwitchClassloader defineSwitchClassloader = new DefineSwitchClassloader(Thread.currentThread().getContextClassLoader());
//
//    /**
//     *
//     */
//    private static final Type nameSpace = Type.getType(NameSpace.class);
//
//    /**
//     *
//     */
//    private static final Type appSwitch = Type.getType(AppSwitch.class);
//
//    /**
//     *
//     */
//    private static final Type levelEnum = Type.getType(Switch.Level.class);
//
//    /**
//     *
//     */
//    private static final Type object = Type.getType(Object.class);
//
//    /**
//     *
//     */
//    private static final String p1 = "p1";
//    private static final String p2 = "p2";
//    private static final String p3 = "p3";
//    private static final String p4 = "p4";
//
//    /**
//     *
//     */
//    public static final String grayFieldName = "userIdGrayRate";
//
//    /**
//     *
//     */
//    private static ClassReader switchTemplateReader = null;
//
//    /**
//     *
//     */
//    protected ClassWriter cw;
//    protected FieldVisitor fw;
//    protected AnnotationVisitor aw;
//
//    static {
//        ScanClassUtils.scan("com.alibaba.trade.receipt.shared.gray", new ScanHandler() {
//            @Override
//            public void handle(Resource resource, MetadataReader metadataReader,
//                    MetadataReaderFactory metadataReaderFactory) throws Exception {
//                if (metadataReader.getClassMetadata().getClassName().equals(
//                        "com.alibaba.trade.receipt.shared.gray.SwitchTemplate")) {
//                    switchTemplateReader = new ClassReader(metadataReader.getResource().getInputStream());
//                }
//            }
//        });
//    }
//
//    public Class<?> generateV2(BusinessDefMetadata businessDefMetadata) {
//        cw = new ClassWriter(0);
//        String className = businessDefMetadata.getTemplateClass().getName() + "__GraySwitch";
//        GrayClassAsmGenerator grayClassAsmGenerator = new GrayClassAsmGenerator(Opcodes.ASM4,cw, businessDefMetadata.getBizCode(), className);
//        switchTemplateReader.accept(grayClassAsmGenerator, 0);
//        return defineSwitchClassloader.defineClass(className, cw.toByteArray());
//    }
//
//    public Class<?> generate(BusinessDefMetadata businessDefMetadata) {
//        cw = null;
//        fw = null;
//        aw = null;
//
//        Class<? extends BusinessTemplate> templateClass = businessDefMetadata.getTemplateClass();
//        String className = templateClass.getName() + "__GraySwitch";
//        String bizCode = businessDefMetadata.getBizCode();
//
//        cw = new ClassWriter(0);
//
//        defineClass(Opcodes.ACC_PUBLIC , className.replace(".","/"), object.getInternalName());
//            $nameSpace(bizCode, bizCode + "的灰度开关");
//            field(Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_VOLATILE,
//                    Type.getDescriptor(int.class),
//                    grayFieldName, 0);
//                $appSwitch("灰度控制0-100, 用户id最后3-4位做控制", p1);
//            endField();
//        classDefineEnd();
//        byte[] bytes = cw.toByteArray();
//        return defineSwitchClassloader.defineClass(className, bytes);
//    }
//
//    private void $appSwitch(String desc, String level) {
//        aw = fw.visitAnnotation(appSwitch.getDescriptor(), true);
//        aw.visit("des", desc);
//        aw.visitEnum("level", levelEnum.getDescriptor(), level);
//        aw.visitEnd();
//    }
//
//    private void classDefineEnd() {
//        cw.visitEnd();
//    }
//
//    private void endField() {
//        fw.visitEnd();
//    }
//
//    private void field(int acc, String typeDesc, String fieldName, Object initValue) {
//        fw = cw.visitField(acc, fieldName, typeDesc, null, initValue);
//    }
//
//    private void defineClass(int access, String className, String superClass) {
//        cw.visit(Opcodes.V1_8, access , className, null, superClass, null);
//    }
//
//    private void $nameSpace(String bizCode, String desc) {
//        aw = cw.visitAnnotation(nameSpace.getDescriptor(), true);
//        aw.visit("nameSpace", bizCode);
//        aw.visit("desc", desc);
//        aw.visitEnd();
//    }
//}
