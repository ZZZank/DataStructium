## DataStructium 1.8.0 -> 1.8.1

Fix Botania crash

- Fix crash when Botania is present, due to Batania modifying models in an unexpected way (not quite unexpected actually, but unexpected for me)

---

## DataStructium 1.7.1 -> 1.8.0

Refining optimization approaches

- Improve: better Oculus support for `Cache Shader Uniforms`
- Add: Ingredient Duplication, disabled by default
- Remove: `Tiered Tag Internal`, this causes additional object creation when ModernFix is installed, and we can't disable ModernFix optimization
- Remove: `Deduplicate CompoundTag Keys`, ModernFix did the same thing
- Add: allow modifying MM structure check interval. By default, DataStructium sets it to be 1 check per 5 ticks (MM default is 1 check per tick)
- Improve: some optimizations are rewritten to patch in a cleaner way
- Add: `Clear empty Chunk section automatically`
- Improve: expand `optimize_small_model`
- Improve: optimize MM structure block match

---

## DataStructium 1.7.0 -> 1.7.1

CompoundTag optimization improvement

- ModernFix detection for CompoundTag check
  - DataStructium optimization will be force-disabled when ModernFix is present. This is not the most ideal solution as
ModernFix's solution is less thorough, but this is more compatible
  - you can disable this check in config
- fast copy for CompoundTag
- smaller Number Tag Cache default size
- fix a certain part of CompoundTag optimization not working

---

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
