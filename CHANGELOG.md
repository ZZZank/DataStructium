## DataStructium 1.6.0 -> 1.7.0

- Fixed BlockPos hashing algorithm breaking rendering if ImmersiveEngineering machines
- slightly improved the efficiency of Shader Uniform cache
- introduce key interning for CompoundTag backend to further decrease memory usage
- Faster chunk section iterating, making client side chunk meshing and possibly other client/server chunk section-based
  operations faster
- Disabling or enabling features will now directly disable/enable the mixin, avoiding redundant injection, also
  simplifying mixin codes
- make many more optimizations toggleable
- removed one useless optimization
- add a developer option to disable all mixins

---
